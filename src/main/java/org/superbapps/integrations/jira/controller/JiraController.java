package org.superbapps.integrations.jira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.superbapps.integrations.jira.dto.business.issues.result.IssueDto;
import org.superbapps.integrations.jira.service.JiraService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/jira")
public class JiraController {

    @Autowired
    JiraService service;

    @GetMapping(value = {"/issue/bykey/{key}"})
    public ResponseEntity jiraHome(@PathVariable("key") String key) {
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(APPLICATION_JSON)
                             .body(service.getIssueByKey(key));
    }

    @GetMapping(value = {"/dashboards"})
    public ResponseEntity getAllDashboards() {
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(APPLICATION_JSON)
                             .body(service.getAllDashboards());
    }

    @GetMapping(value = "/projects/{issueName}")
    public ResponseEntity getProject(@PathVariable(value = "issueName") String key) {
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(APPLICATION_JSON)
                             .body(service.getProjectByKey(key));
    }

    @GetMapping(value = {"/projects"})
    public ResponseEntity getAllProjects(Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(APPLICATION_JSON)
                             .body(service.getAllProjects());
    }

    @GetMapping(value = {"/issuetypes"})
    public ResponseEntity getAllIssueTypes() {
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(APPLICATION_JSON)
                             .body(service.getAllIssueTypes());
    }

    @GetMapping(value = {"/issuetype/key/{issueKey}"})
    public ResponseEntity getIssueType(@PathVariable("issueKey") String key) {
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(APPLICATION_JSON)
                             .body(service.getIssueType(key));
    }

    @GetMapping(value = {"/issuetype/name/{issueName}"})
    public ResponseEntity getIssueTypeForName(@PathVariable("issueName") String name) {
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(APPLICATION_JSON)
                             .body(service.getIssueTypeForName(name));
    }

    @PostMapping(value = "/issues/create", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createIssue(@RequestBody IssueDto issueDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                                 .contentType(APPLICATION_JSON)
                                 .body(service.createIssue(issueDto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
