package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.services.TaskDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/taskDomain")
@RequiredArgsConstructor
public class TaskDomainController {

    private final TaskDomainService taskDomainService;

    @Operation(description = "Method that create a new task domain")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @PostMapping("/")
    public TaskDomain create(
            @RequestBody TaskDomain taskDomain) {
        return taskDomainService.create(taskDomain);
    }
}
