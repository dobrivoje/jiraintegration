package org.superbapps.integrations.jira.dto.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {

	String id;
	String name;
	String self;
	String view;
}
