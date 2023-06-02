package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

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
