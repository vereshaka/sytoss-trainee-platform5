package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.services.TaskService;
import com.sytoss.lessons.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TopicController {

    private final TopicService topicService;

    private final TaskService taskService;

    @Operation(description = "Method that retrieve information about topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suceess|OK"),
    })
    @GetMapping("/discipline/{disciplineId}/topics")
    public List<Topic> findByDisciplineId(
            @PathVariable(value = "disciplineId") Long discipleId) {
        return topicService.findByDiscipline(discipleId);
    }

    @Operation(description = "Method that return topic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suceess|OK"),
            @ApiResponse(responseCode = "404", description = "Discipline not found!"),
    })
    @GetMapping("/topic/{topicId}")
    public Topic getById(
            @Parameter(description = "id of topic to be searched")
            @PathVariable(value = "topicId")
            Long topicId) {
        return topicService.getById(topicId);
    }

    @Operation(description = "Method that retrieve task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task not found!"),
            @ApiResponse(responseCode = "404", description = "Topic not found!")
    })
    @PostMapping("/task/{taskId}/topic/{topicId}")
    public Task assignTaskToTopic(@Parameter(description = "id of the task to be searched by")
                                  @PathVariable("taskId")
                                  Long taskId, @Parameter(description = "id of the task to be searched by")
                                  @PathVariable("topicId")
                                  Long topicId) {
        return taskService.assignTaskToTopic(taskId, topicId);
    }

    @Operation(description = "Method that gets tasks by topic ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "404", description = "Topic not found")
            })
    @GetMapping("/topic/{topicId}/tasks")
    public List<Task> findByTopicId(
            @PathVariable(value = "topicId") Long topicId) {
        return taskService.findByTopicId(topicId);
    }
}
