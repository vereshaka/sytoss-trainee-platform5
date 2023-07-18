package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.lessons.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task")
@PreAuthorize("isAuthenticated()")
public class TaskController {

    private final TaskService taskService;

    @Operation(description = "Method that retrieve task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task not found!")
    })
    @GetMapping("/{taskId}")
    public Task getById(@Parameter(description = "id of the task to be searched by")
                        @PathVariable("taskId")
                        Long taskId) {
        return taskService.getById(taskId);
    }

    @Operation(description = "Method that create task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Task already exist!")
    })
    @PostMapping("")
    public Task create(@RequestBody Task task) {
        return taskService.create(task);
    }

    @Operation(description = "Method that remove condition from task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task not found!"),
            @ApiResponse(responseCode = "404", description = "Task condition not found!"),
            @ApiResponse(responseCode = "404", description = "Task don't has condition!")

    })
    @PutMapping("/{taskId}/condition/{conditionId}")
    public Task removeCondition(@Parameter(description = "id of the task to be searched by")
                                @PathVariable("taskId")
                                Long taskId,
                                @Parameter(description = "id of the task to be searched by")
                                @PathVariable("conditionId")
                                Long conditionId) {
        return taskService.removeCondition(taskId, conditionId);
    }

    @Operation(description = "Method that add condition to task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Task condition already exist!")
    })
    @PutMapping("/{taskId}/condition")
    public Task addCondition(@Parameter(description = "id of the task to be searched by")
                             @PathVariable("taskId")
                             Long taskId,
                             @RequestBody TaskCondition taskCondition) {
        return taskService.addCondition(taskId, taskCondition);
    }

    @Operation(description = "Retrieve data with SQL request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/check-request-result")
    public QueryResult getQueryResult(@RequestBody CheckRequestParameters checkRequestParameters) {
        return taskService.getQueryResult(checkRequestParameters);
    }
}
