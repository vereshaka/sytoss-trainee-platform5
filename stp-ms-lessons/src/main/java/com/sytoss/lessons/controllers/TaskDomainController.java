package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.services.TaskDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskDomainController {

    private final TaskDomainService taskDomainService;

    @Operation(description = "Method that save information about exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task domain not found"),
    })
    @GetMapping("/taskDomain/{taskDomainId}")
    public TaskDomain getById(@Parameter(description = "id of the task domain to be searched by")
                              @PathVariable(value = "taskDomainId") Long taskDomainId) {
        return taskDomainService.getById(taskDomainId);
    }
}
