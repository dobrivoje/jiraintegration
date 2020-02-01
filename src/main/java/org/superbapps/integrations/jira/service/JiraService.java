package org.superbapps.integrations.jira.service;

import com.google.api.client.util.GenericData;
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
import org.superbapps.integrations.jira.infra.JiraClient;

import java.text.MessageFormat;
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
    //</editor-fold>

    //<editor-fold desc="JIRA API calls business implementations">
    public JiraSingleResultIssueDto getIssueByKey(String key) {
        String issueApiMethodCallUrl = MessageFormat.format(apiCallIssueByKey, key);
        JiraSingleResultIssueDto dto = jiraClient.executeGet(JiraSingleResultIssueDto.class, issueApiMethodCallUrl);
        if (dto == null) {
            throw new RuntimeException("Jira issue with key " + key + ", does not exist.");
        }

        return dto;
    }

    public AllDashboardsDto getAllDashboards() {
        return jiraClient.executeGet(AllDashboardsDto.class, apiCallAllDashboard);
    }

    public List<ProjectDto> getAllProjects() {
        List<ProjectDto> projects = jiraClient.executeGetExpectingList(apiCallAllProjects, ProjectDto.class);
        return projects;
    }

    public ProjectDto getProjectByKey(Object key) {
        ProjectDto project = jiraClient.executeGet(ProjectDto.class, MessageFormat.format(apiCallProjectById, String.valueOf(key)));
        if (project == null) {
            throw new RuntimeException("Project with key " + key + ", does not exist.");
        }

        return project;
    }

    public List<JiraIssueTypeDto> getAllIssueTypes() {
        List<JiraIssueTypeDto> issueTypes = jiraClient.executeGetExpectingList(apiCallAllIssueTypes, JiraIssueTypeDto.class);
        return issueTypes;
    }

    public JiraIssueTypeDto getIssueType(Object key) {
        JiraIssueTypeDto issueType = jiraClient.executeGet(JiraIssueTypeDto.class, MessageFormat.format(apiCallApiIssueType, String.valueOf(key)));
        if (issueType == null)
            throw new RuntimeException("Jira issue type with key " + key + ", does not exist.");

        return issueType;
    }

    public IssueCreatedResponseDto createIssue(IssueDto issueDto) throws Exception {
        GenericData issueData = new GenericData();

        try {
            // check for existing Project, and carry on if it exists...
            ProjectDto projectDto = getProjectByKey(issueDto.getFields().getProject().getId());
            GenericData projectData = new GenericData();
            projectData.put("key", projectDto.getKey());

            // check for existing issue type, and carry on with no errors..
            Long issueId = issueDto.getFields().getIssuetype().getId();
            getIssueType(issueId);
            GenericData issueTypeData = new GenericData();
            issueTypeData.put("id", issueId);

            GenericData fieldsData = new GenericData();
            fieldsData.set("summary", issueDto.getFields().getSummary());
            fieldsData.set("description", issueDto.getFields().getDescription());

            fieldsData.set("issuetype", issueTypeData);
            fieldsData.set("project", projectData);

            issueData.put("fields", fieldsData);

            IssueCreatedResponseDto issueResponse = jiraClient.executePost(IssueCreatedResponseDto.class, apiCallIssueCreate, issueData);
            return issueResponse;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    //</editor-fold>
}
