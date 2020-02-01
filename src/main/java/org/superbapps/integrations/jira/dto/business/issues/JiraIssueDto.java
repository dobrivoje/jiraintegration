package org.superbapps.integrations.jira.dto.business.issues;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JiraIssueDto {

	Long         id;
	String       key;
	String       subject;
	String       description;
	String       portfolio;
	String       status;
	String       siteName;
	Long         siteId;
	List<String> components = new ArrayList<>();
}
