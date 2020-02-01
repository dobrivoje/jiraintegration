package org.superbapps.integrations.jira.infra;

import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.GenericData;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.superbapps.integrations.jira.infra.oauth.JiraOAuthClient;
import org.superbapps.integrations.utils.JSONUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JiraClient {

    private static final Logger LOGGER = Logger.getLogger(JiraClient.class.getName());

    //<editor-fold desc="oauth 1.0 credentials">
    @Value("${jira_home}")
    String JIRA_HOME_URL;

    @Value("${jira_base_url}")
    String JIRA_ENDPOINT_URL;

    @Value("${jira_access_token}")
    String JIRA_ACCESS_TOKEN;

    @Value("${jira_secret}")
    String JIRA_SECRET_KEY;

    @Value("${jira_consumer_key}")
    String JIRA_CONSUMER_KEY;

    @Value("${jira_private_key}")
    String JIRA_PRIVATE_KEY;

    @Value("${datetimeformat}")
    private String dateTimeFormat;

    JSONUtils jsonUtils;

    JiraOAuthClient jiraOAuthClient;
    //</editor-fold>

    //<editor-fold desc="jiraOAuthClient initialization.">
    @PostConstruct
    void jiraOAuthClientInit() {
        if (jiraOAuthClient == null) {
            try {
                jiraOAuthClient = new JiraOAuthClient(JIRA_HOME_URL);
            } catch (Exception e) {
                String errMsg = "Jira OAuth Client Error.";
                LOGGER.log(Level.WARNING, errMsg, e);
                throw new RuntimeException(errMsg + e);
            }
        }

    }
    //</editor-fold>

    //<editor-fold desc="Primitive GET/POST Request support methods.">
    private HttpResponse getResponseFromUrl(OAuthParameters parameters, GenericUrl jiraUrl) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(parameters);
        HttpRequest request = requestFactory.buildGetRequest(jiraUrl);
        return request.execute();
    }

    private HttpResponse postResponseFromUrl(OAuthParameters parameters, GenericUrl jiraUrl, HttpContent requestContent) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(parameters);
        HttpRequest request = requestFactory.buildPostRequest(jiraUrl, requestContent);
        return request.execute();
    }
    //</editor-fold>

    //<editor-fold desc="Supporting low-level {@link HttpResponse}'s">

    /**
     * This is the most basic way to issue a GET call.<br>
     *
     * @param url Parameter must be properly designed, meaning, whole URL including end-point, api version,<br>
     *            API GET call name, and eventual parameters must be manually created.<br>
     */
    private HttpResponse handleGetRequest(String url) {
        try {
            OAuthParameters parameters = jiraOAuthClient.getParameters(JIRA_ACCESS_TOKEN, JIRA_SECRET_KEY, JIRA_CONSUMER_KEY, JIRA_PRIVATE_KEY);
            HttpResponse response = getResponseFromUrl(parameters, new GenericUrl(url));
            return response;
        } catch (Exception e) {
            String errMsg = "Handle GetRequest Error.";
            LOGGER.log(Level.WARNING, errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }

    /**
     * This is preferred and convenient way to use a GET call, for results of type {@link HttpResponse}<br>
     *
     * @param apiMethodCallUrl Only Jira API call with belonging parameters should be created.<br>
     */
    private HttpResponse executeGetAndReturnHttpResponse(@NonNull String apiMethodCallUrl) {
        return handleGetRequest(JIRA_ENDPOINT_URL + apiMethodCallUrl);
    }
    //</editor-fold>

    // End-user (developer) API calls
    //<editor-fold desc="GET">

    /**
     * Execute Jira API GET call, and return result of a type {@link T}<br>
     * Result is parsed to a Dto of {@link T} type.<br>
     * For a result which is of type List<?>, see {@link #executeGetExpectingList}
     *
     * @param apiMethodCallUrl API call with Url defined by the JIRA API.<br>
     *                         Usage example :<br>
     *                         Api call, and two parameters defined at {0} and {1} positions:<br>
     *                         SITE_ID_AND_PORTFOLIO_SEARCH_JQL_WITH_OR=Portfolio = {0} AND ("Site ID" ~ "0"{1})<br>
     *                         For proper usage, MessageFormat.format( apiCall_Name, apiCall_Parameters) may be used.<br>
     */
    public <T> T executeGet(Class<T> clazz, String apiMethodCallUrl) {
        try {
            HttpResponse jsonResponse = executeGetAndReturnHttpResponse(apiMethodCallUrl);
            if (jsonResponse == null) {
                return null;
            }

            return jsonUtils.parseResponse(jsonResponse, clazz);
        } catch (Exception e) {
            String errMsg = "Executing Get Request Error.";
            LOGGER.log(Level.SEVERE, errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }

    /**
     * Execute Jira API GET call, and return result of a type {@link List<T>}<br>
     * Result is parsed to a list of Dto's of "T" type.<br>
     */
    public <T> List<T> executeGetExpectingList(@NonNull String apiMethodCallUrl, Class<T> resultTypeList) {
        try {
            HttpResponse jsonResponse = executeGetAndReturnHttpResponse(apiMethodCallUrl);
            if (jsonResponse == null) {
                return null;
            }

            return jsonUtils.parseResponseAsList(jsonResponse, resultTypeList, JSONUtils.getDateTimeFormat(dateTimeFormat));
        } catch (Exception e) {
            String errMsg = "Executing Get Request Error.";
            LOGGER.log(Level.SEVERE, errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }
    //</editor-fold>

    //<editor-fold desc="POST">

    /**
     * This is preffered and convenient way to use a POST call, for results of type {@link HttpResponse}<br>
     *
     * @param postOperationName  Jira POST API call name.<br>
     * @param contentGenericData POST parameters for the call.<br>
     */
    public HttpResponse executePostRequest(@NonNull String postOperationName, @NonNull GenericData contentGenericData) {
        String apiCallUrlPath = JIRA_ENDPOINT_URL + postOperationName;

        try {
            OAuthParameters parameters = jiraOAuthClient.getParameters(JIRA_ACCESS_TOKEN, JIRA_SECRET_KEY, JIRA_CONSUMER_KEY, JIRA_PRIVATE_KEY);
            HttpContent content = new JsonHttpContent(new JacksonFactory(), contentGenericData);
            HttpResponse response = postResponseFromUrl(parameters, new GenericUrl(apiCallUrlPath), content);

            return response;
        } catch (HttpResponseException hre) {
            String errMsg = "Executing Post Request Error. " + hre;
            LOGGER.log(Level.SEVERE, errMsg, hre);
            throw new RuntimeException(errMsg, hre);
        } catch (Exception e) {
            String errMsg = "Executing Get Request, no result.";
            LOGGER.log(Level.INFO, errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }

    /**
     * This is preferred and convenient way to use a POST call, for results of type {@link T}<br>
     *
     * @param postOperationName  Jira POST API call name.<br>
     * @param contentGenericData POST parameters for the call.<br>
     */
    public <T> T executePost(Class<T> clazz, @NonNull String postOperationName, @NonNull GenericData contentGenericData) {
        try {
            HttpResponse jsonResponse = executePostRequest(postOperationName, contentGenericData);
            if (jsonResponse == null) {
                return null;
            }

            return jsonUtils.parseResponse(jsonResponse, clazz);
        } catch (Exception e) {
            String errMsg = "Executing Post Request Error.";
            LOGGER.log(Level.WARNING, errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }
    //</editor-fold>
}
