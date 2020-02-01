package org.superbapps.integrations.jira.dto.business.issues.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.superbapps.integrations.jira.dto.business.issues.IssueUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueDto {

	IssueUpdateDto update;
	IssueFieldsDto fields;
}
