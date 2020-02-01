package org.superbapps.integrations.jira.dto.business.issues;

import lombok.Data;

@Data
public class JiraIssueTypeDto {

	String  self;
	String  id;
	String  description;
	String  iconUrl;
	String  name;
	Boolean subtask;
	Long    avatarId;
}
