package org.superbapps.integrations.jira.dto.generic;

import org.superbapps.integrations.jira.dto.business.issues.JiraIssueFieldsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JiraSingleResultIssueDto {

	Long               id;
	String             key;
	JiraIssueFieldsDto fields;
}
