package org.superbapps.integrations.jira.dto.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JiraComponentFieldDto {

	Long   id;
	String self;
	String name;
}
