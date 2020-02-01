package org.superbapps.integrations.jira.dto.business.issues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.superbapps.integrations.jira.dto.business.issues.parts.WorkLogDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueUpdateDto {

	WorkLogDto worklog;
}
