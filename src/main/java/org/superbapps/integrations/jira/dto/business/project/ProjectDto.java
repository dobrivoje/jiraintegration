package org.superbapps.integrations.jira.dto.business.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.superbapps.integrations.jira.dto.generic.basic.JiraSelfIdNameDescDto;

@JsonIgnoreProperties(ignoreUnknown = true)

@Data

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectDto {

	String                self;
	String                id;
	String                key;
	String                name;
	ProjectAvatarDto      avatarUrls;
	JiraSelfIdNameDescDto projectCategory;
	String                projectTypeKey;
}
