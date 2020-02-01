package org.superbapps.integrations.jira.dto.generic.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.superbapps.integrations.jira.dto.generic.JiraComponentFieldDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)

@Data
@NoArgsConstructor
public class JiraSelfIdNameDescDto extends JiraComponentFieldDto {

	String description;

	public JiraSelfIdNameDescDto(Long id, String self, String name, String description) {
		super( id, self, name );
		this.description = description;
	}
}
