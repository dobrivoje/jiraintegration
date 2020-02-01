package org.superbapps.integrations.jira.dto.business.issues;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JiraIssueSearchRequestDto {

	String       jql;
	//	int maxResults;
	List<String> fields = new ArrayList<>();
	int          startAt;
}
