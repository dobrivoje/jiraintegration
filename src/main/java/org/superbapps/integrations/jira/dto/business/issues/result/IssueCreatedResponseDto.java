package org.superbapps.integrations.jira.dto.business.issues.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)

@Data
@AllArgsConstructor
public class IssueCreatedResponseDto {

	Long   id;
	String key;
	String self;
}
