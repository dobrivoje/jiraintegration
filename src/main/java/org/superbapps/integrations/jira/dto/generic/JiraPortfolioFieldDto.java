package org.superbapps.integrations.jira.dto.generic;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JiraPortfolioFieldDto {

	@SerializedName("value")
	String portfolioName;

	Long id;
}
