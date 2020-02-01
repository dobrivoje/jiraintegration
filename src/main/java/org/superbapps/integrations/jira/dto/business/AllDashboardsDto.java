package org.superbapps.integrations.jira.dto.business;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllDashboardsDto {

	Float              startAt;
	Float              maxResults;
	Float              total;
	String             prev;
	String             next;
	List<DashboardDto> dashboards = new ArrayList<>();
}
