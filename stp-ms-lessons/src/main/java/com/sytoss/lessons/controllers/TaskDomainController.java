package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.services.TaskDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/taskDomain")
@RequiredArgsConstructor
public class TaskDomainController {

    private final TaskDomainService taskDomainService;

    @Operation(description = "Method that save information about exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task domain not found"),
    })
    @GetMapping("/{taskDomainId}")
    public TaskDomain getById(@Parameter(description = "id of the task domain to be searched by")
                              @PathVariable(value = "taskDomainId") Long taskDomainId) {
        return taskDomainService.getById(taskDomainId);
    }

    @Operation(description = "Method that update task domain")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task domain not found"),
            @ApiResponse(responseCode = "404", description = "Task domain is used"),
    })
    @PutMapping("/{taskDomainId}")
    public TaskDomain update(@Parameter(description = "id of the task domain to be searched by")
                              @PathVariable(value = "taskDomainId") Long taskDomainId,
                             @RequestBody TaskDomain taskDomain) {
        return taskDomainService.update(taskDomainId, taskDomain);
    }
}
