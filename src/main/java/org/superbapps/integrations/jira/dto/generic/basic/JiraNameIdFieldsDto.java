package org.superbapps.integrations.jira.dto.generic.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraNameIdFieldsDto {

	String name;
	Long   id;
}