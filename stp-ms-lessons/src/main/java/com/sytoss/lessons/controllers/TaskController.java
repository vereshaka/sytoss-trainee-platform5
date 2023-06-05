package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    @Operation(description = "Method that retrieve task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task not found!")
    })
    @GetMapping("/task/{taskId}")
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
    @PostMapping("/task/")
    public Task create(@RequestBody Task task) {
        return taskService.create(task);
    }

    @Operation(description = "Method that gets tasks by topic ID")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "Success|OK"), @ApiResponse(responseCode = "404",
                    description = "Topic not found")})
    @GetMapping("/topic/{topicId}/tasks")
    public List<Task> getByTopicId(
            @PathVariable(value = "topicId") Long topicId) {
        return taskService.findByTopicId(topicId);
    }
}
