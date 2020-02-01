package org.superbapps.integrations.jira.dto.business.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectAvatarDto {

	@JsonProperty("48x48")
	String x48;

	@JsonProperty("32x32")
	String x32;

	@JsonProperty("24x24")
	String x24;

	@JsonProperty("16x16")
	String x16;
}
