package org.superbapps.integrations.jira.controller;

import org.superbapps.integrations.jira.dto.business.issues.result.IssueDto;
import org.superbapps.integrations.jira.service.JiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/jira")
public class JiraController {

	@Autowired
	JiraService service;

	@GetMapping(value = { "/getIssueByKey/{key}" })
	public ResponseEntity jiraHome(@PathVariable("key") String key) {
		return ResponseEntity.status( HttpStatus.OK )
							 .contentType( APPLICATION_JSON )
							 .body( service.getIssueByKey( key ) );
	}

	@GetMapping(value = { "/dashboards" })
	public ResponseEntity getAllDashboards() {
		return ResponseEntity.status( HttpStatus.OK )
							 .contentType( APPLICATION_JSON )
							 .body( service.getAllDashboards() );
	}

	@GetMapping(value = "/projects/{key}")
	public ResponseEntity getProject(@PathVariable(value = "key") String key) {
		return ResponseEntity.status( HttpStatus.OK )
							 .contentType( APPLICATION_JSON )
							 .body( service.getProjectByKey( key ) );
	}

	@GetMapping(value = { "/projects" })
	public ResponseEntity getAllProjects(Long id) {
		return ResponseEntity.status( HttpStatus.OK )
							 .contentType( APPLICATION_JSON )
							 .body( service.getAllProjects() );
	}

	@GetMapping(value = { "/issuetypes" })
	public ResponseEntity getAllIssueTypes() {
		return ResponseEntity.status( HttpStatus.OK )
							 .contentType( APPLICATION_JSON )
							 .body( service.getAllIssueTypes() );
	}

	@GetMapping(value = { "/issuetype/{key}" })
	public ResponseEntity getIssueType(@PathVariable("key") String key) {
		return ResponseEntity.status( HttpStatus.OK )
							 .contentType( APPLICATION_JSON )
							 .body( service.getIssueType( key ) );
	}

	@PostMapping(value = "/issues/create", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity createIssue(@RequestBody IssueDto issueDto) {
		try {
			return ResponseEntity.status( HttpStatus.OK )
								 .contentType( APPLICATION_JSON )
								 .body( service.createIssue( issueDto ) );
		} catch (Exception e) {
			throw new RuntimeException( e );
		}
	}
}
