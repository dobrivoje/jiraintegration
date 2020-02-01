package org.superbapps.integrations.jira.dto.business.issues;

import org.superbapps.integrations.jira.dto.generic.JiraSingleResultIssueDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JiraIssueSearchResultDto {

	Long                           total;
	int                            startAt;
	List<JiraSingleResultIssueDto> issues;
}
