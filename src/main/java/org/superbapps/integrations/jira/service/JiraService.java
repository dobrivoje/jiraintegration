package org.superbapps.integrations.jira.service;

import com.google.api.client.util.GenericData;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.superbapps.integrations.jira.dto.business.AllDashboardsDto;
import org.superbapps.integrations.jira.dto.business.issues.JiraIssueTypeDto;
import org.superbapps.integrations.jira.dto.business.issues.result.IssueCreatedResponseDto;
import org.superbapps.integrations.jira.dto.business.issues.result.IssueDto;
import org.superbapps.integrations.jira.dto.business.project.ProjectDto;
import org.superbapps.integrations.jira.dto.generic.JiraSingleResultIssueDto;
import org.superbapps.integrations.jira.dto.generic.JiraUserDto;
import org.superbapps.integrations.jira.infra.JiraClient;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@PropertySource("classpath:/JiraApiCalls/calls.properties")
@Service
public class JiraService {

    @Autowired
    JiraClient jiraClient;

    //<editor-fold desc="Api Business Calls Definitions">
    @Value("${GET_ISSUE_BY_KEY_URL}")
    String apiCallIssueByKey;

    @Value("${SITE_ID_AND_PORTFOLIO_SEARCH_JQL_WITH_OR}")
    String apiCallSiteIdAndPortfolioSearchJQLWithOr;

    @Value("${OR_SITE_ID}")
    String apiCallOrSiteId;

    @Value("${SEARCH_ISSUES_URL}")
    String searchIssuesKey;

    @Value("${DASHBOARDS}")
    String apiCallAllDashboard;

    @Value("${PROJECTS}")
    String apiCallAllProjects;

    @Value("${PROJECT}")
    String apiCallProjectById;

    @Value("${ISSUETYPES}")
    String apiCallAllIssueTypes;

    @Value("${ISSUETYPE}")
    String apiCallApiIssueType;

    @Value("${ISSUE_CREATE}")
    String apiCallIssueCreate;

    @Value("${find_bulk_assignable_users}")
    String apiCallAssignableUsers;
    //</editor-fold>

    //<editor-fold desc="JIRA API calls business implementations">
    public JiraSingleResultIssueDto getIssueByKey(String key) {
        String issueById = MessageFormat.format(apiCallIssueByKey, key);
        JiraSingleResultIssueDto dto = jiraClient.executeGet(JiraSingleResultIssueDto.class, issueById);

        if (dto == null)
            throw new RuntimeException("Jira issue with key " + key + ", does not exist.");

        return dto;
    }

    public List<JiraIssueTypeDto> getAllIssueTypes() {
        return jiraClient.executeGetExpectingList(apiCallAllIssueTypes, JiraIssueTypeDto.class);
    }

    public JiraIssueTypeDto getIssueTypeForName(@NonNull String issueTypeName) {
        return getAllIssueTypes().stream()
                                 .filter(f -> issueTypeName.equalsIgnoreCase(f.getName()))
                                 .findFirst()
                                 .orElseThrow(() -> new RuntimeException(
                                     "Jira issue type name : " + issueTypeName + ", not found."));
    }

    public JiraIssueTypeDto getIssueType(Object key) {
        JiraIssueTypeDto issueType = jiraClient.executeGet(JiraIssueTypeDto.class,
                                                           MessageFormat.format(apiCallApiIssueType, String.valueOf(key)));
        if (issueType == null)
            throw new RuntimeException("Jira issue type with key " + key + ", does not exist.");

        return issueType;
    }

    public AllDashboardsDto getAllDashboards() {
        return jiraClient.executeGet(AllDashboardsDto.class, apiCallAllDashboard);
    }

    public List<ProjectDto> getAllProjects() {
        return jiraClient.executeGetExpectingList(apiCallAllProjects, ProjectDto.class);
    }

    public ProjectDto getProjectByKey(Object key) {
        ProjectDto project = jiraClient.executeGet(ProjectDto.class, MessageFormat.format(apiCallProjectById, String.valueOf(key)));
        if (project == null)
            throw new RuntimeException("Project with key " + key + ", does not exist.");

        return project;
    }

    public List<JiraUserDto> findBulkAssignableUsers(@NonNull String userName, @NonNull String projectKeys, Integer maxResult) {
        // validateProjectKeys
        String keys = projectKeys.trim();
        String[] pkeys = keys.split(",");

        String params = MessageFormat.format(apiCallAssignableUsers, userName, projectKeys,
                                             maxResult == null ? 50 : maxResult);
        List<JiraUserDto> resDto = jiraClient.executeGetExpectingList(params, JiraUserDto.class);

        if (resDto == null)
            throw new RuntimeException("Bulk Assignable Users for username " + userName +
                                           ", and projects " + Arrays.toString(pkeys) + ", not exist.");

        return resDto;
    }

    public IssueCreatedResponseDto createIssueForProjectIssueTypeId(String projectKey, long issueTypeId,
                                                                    String summary, String description) throws Exception {
        try {
            GenericData fieldsData = new GenericData();

            // validate project key :
            getProjectByKey(projectKey);
            GenericData projectData = new GenericData();
            // For project "key" must be supplied :
            projectData.put("key", projectKey);
            fieldsData.set("project", projectData);

            fieldsData.set("summary", summary);
            fieldsData.set("description", description);

            // validate supplied issue type :
            GenericData issueTypeData = new GenericData();
            // validate supplied issue type :
            issueTypeData.put("id", getIssueType(issueTypeId).getId());
            fieldsData.set("issuetype", issueTypeData);

            // in the end, set all fields and execute POST request
            GenericData issueData = new GenericData();
            issueData.put("fields", fieldsData);

            return jiraClient.executePost(IssueCreatedResponseDto.class, apiCallIssueCreate, issueData);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public IssueCreatedResponseDto createIssueForProjectIssueType(String projectKey, String issueTypeName, String summary, String description) throws Exception {
        JiraIssueTypeDto typeDto = getIssueTypeForName(issueTypeName);
        return createIssueForProjectIssueTypeId(projectKey, Long.valueOf(typeDto.getId()), summary, description);
    }

    public IssueCreatedResponseDto createIssue(IssueDto issueDto) throws Exception {
        ProjectDto projectDto = getProjectByKey(issueDto.getFields().getProject().getId());
        Long dtoIssueId = issueDto.getFields().getIssuetype().getId();

        return createIssueForProjectIssueTypeId(projectDto.getKey(), dtoIssueId, issueDto.getFields().getSummary(), issueDto.getFields().getDescription());
    }
    //</editor-fold>
}
