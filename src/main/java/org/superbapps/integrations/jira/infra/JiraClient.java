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

    @Value("${jira_api_v2:/rest/api/2/}")
    String JIRA_API_V2_URL;

    @Value("${jira_api_v3:/rest/api/3/}")
    String JIRA_API_V3_URL;

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
     * @param apiCallWithParams Only Jira API call with belonging parameters should be created.<br>
     */
    private HttpResponse executeGetBasicAndReturnHttpResponse(@NonNull String apiCallWithParams, @NonNull String apiApiVersion) {
        return handleGetRequest(JIRA_HOME_URL.concat(apiApiVersion).concat(apiCallWithParams));
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
        return executeGetBasic(clazz, JIRA_API_V2_URL, apiMethodCallUrl);
    }

    /**
     * @param clazz             Class representation of T type<br>
     * @param apiVersion        As of writing, /rest/api/2/, and /rest/api/3/<br>
     * @param apiCallWithParams End API call, formatted as path call + custom parameter value.<br>
     * @param <T>               Custom type
     */
    public <T> T executeGetBasic(Class<T> clazz, String apiVersion, String apiCallWithParams) {
        try {
            HttpResponse jsonResponse = executeGetBasicAndReturnHttpResponse(apiCallWithParams, apiVersion);
            if (jsonResponse == null) {
                return null;
            }

            return JSONUtils.parseResponse(jsonResponse, clazz);
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
            HttpResponse jsonResponse = executeGetBasicAndReturnHttpResponse(apiMethodCallUrl, JIRA_API_V2_URL);
            if (jsonResponse == null) {
                return null;
            }

            return JSONUtils.parseResponseAsList(jsonResponse, resultTypeList, JSONUtils.getDateTimeFormat(dateTimeFormat));
        } catch (Exception e) {
            String errMsg = "Executing Get Request Error.";
            LOGGER.log(Level.SEVERE, errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }
    //</editor-fold>

    //<editor-fold desc="POST">

    /**
     * This is general way to use a POST calls, for results of type {@link HttpResponse}<br>
     *
     * @param postOperationName  Jira POST API call name.<br>
     * @param apiVersion         Jira API version.<br>
     * @param contentGenericData POST parameters for the call.<br>
     */
    public HttpResponse executePostRequest(@NonNull String postOperationName, @NonNull String apiVersion, @NonNull GenericData contentGenericData) {
        String apiCallUrlPath = JIRA_HOME_URL + apiVersion + postOperationName;

        try {
            OAuthParameters parameters = jiraOAuthClient.getParameters(JIRA_ACCESS_TOKEN, JIRA_SECRET_KEY,
                                                                       JIRA_CONSUMER_KEY, JIRA_PRIVATE_KEY);
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
     * This is general way to use a POST call for supplied API version, for results of type {@link T}<br>
     *
     * @param clazz              Class representation of T type.<br>
     * @param postOperationName  Jira POST API call name.<br>
     * @param apiVersion         Jira API version.<br>
     * @param contentGenericData POST parameters for the call.<br>
     */
    public <T> T executeBasicPost(Class<T> clazz, @NonNull String postOperationName, @NonNull String apiVersion, @NonNull GenericData contentGenericData) {
        try {
            HttpResponse jsonResponse = executePostRequest(postOperationName, apiVersion, contentGenericData);
            if (jsonResponse == null) {
                return null;
            }

            return JSONUtils.parseResponse(jsonResponse, clazz);
        } catch (Exception e) {
            String errMsg = "Executing Post Request Error.";
            LOGGER.log(Level.WARNING, errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }

    /**
     * As most calls are API version 2 calls,<br>
     * this is preferred and convenient way to use those POST calls.<br>
     * Result is parsed to a dto of type {@link T}<br>
     *
     * @param clazz              Class representation of T type.<br>
     * @param postOperationName  Jira POST API call name.<br>
     * @param contentGenericData POST parameters for the call.<br>
     */
    public <T> T executePost(Class<T> clazz, @NonNull String postOperationName, @NonNull GenericData contentGenericData) {
        return executeBasicPost(clazz, postOperationName, JIRA_API_V2_URL, contentGenericData);
    }
    //</editor-fold>
}
