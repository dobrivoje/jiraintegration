package org.superbapps.integrations.jira.dto.business.issues.parts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueAddDto {

	String timeSpent;
	Date   started;
}
