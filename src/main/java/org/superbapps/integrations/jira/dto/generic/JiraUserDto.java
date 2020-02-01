package org.superbapps.integrations.jira.dto.generic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JiraUserDto {

	String displayName;
	String emailAddress;
}
