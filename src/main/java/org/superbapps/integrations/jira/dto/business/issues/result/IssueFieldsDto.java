package org.superbapps.integrations.jira.dto.business.issues.result;

import org.superbapps.integrations.jira.dto.business.issues.parts.IssueTimeTrackingDto;
import org.superbapps.integrations.jira.dto.generic.basic.JiraIdDto;
import org.superbapps.integrations.jira.dto.generic.basic.JiraNameDto;
import org.superbapps.integrations.jira.dto.generic.basic.JiraValueDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueFieldsDto {

	JiraIdDto            project;
	String               summary;
	JiraIdDto            issuetype;
	JiraNameDto          assignee;
	JiraNameDto          reporter;
	JiraIdDto            priority;
	List<String>         labels            = new ArrayList<>();
	IssueTimeTrackingDto timetracking;
	JiraIdDto            security;
	List<JiraIdDto>      versions          = new ArrayList<>();
	String               environment;
	String               description;
	Date                 duedate;
	List<JiraIdDto>      fixVersions       = new ArrayList<>();
	List<JiraIdDto>      components        = new ArrayList<>();
	List<String>         customfield_30000 = new ArrayList<>();
	JiraValueDto         customfield_80000;
	Date                 customfield_20000;
	String               customfield_40000;
	List<String>         customfield_70000 = new ArrayList<>();
	String               customfield_60000;
	String               customfield_50000;
	Date                 customfield_10000;
}