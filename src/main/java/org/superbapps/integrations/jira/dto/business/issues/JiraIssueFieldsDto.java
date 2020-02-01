package org.superbapps.integrations.jira.dto.business.issues;

import com.google.gson.annotations.SerializedName;
import org.superbapps.integrations.jira.dto.generic.JiraComponentFieldDto;
import org.superbapps.integrations.jira.dto.generic.JiraPortfolioFieldDto;
import org.superbapps.integrations.jira.dto.generic.JiraUserDto;
import org.superbapps.integrations.jira.dto.generic.basic.JiraNameIdFieldsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JiraIssueFieldsDto {

	//JiraCommentDto
	//priority
	JiraNameIdFieldsDto status;
	JiraUserDto creator;
	JiraUserDto         reporter;
	JiraUserDto         assignee;
	String              description;
	String              summary;

	@SerializedName("customfield_11110")
	String siteId;

	@SerializedName("customfield_11126")
    JiraPortfolioFieldDto portfolioField;

	List<JiraComponentFieldDto> components;
}
