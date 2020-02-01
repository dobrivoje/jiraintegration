package org.superbapps.integrations.jira.dto.business.issues.parts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueTimeTrackingDto {

	String originalEstimate;
	String remainingEstimate;
}
