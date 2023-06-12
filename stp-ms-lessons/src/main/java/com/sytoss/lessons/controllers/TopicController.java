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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TopicController {

    private final TopicService topicService;

    private final TaskService taskService;

    @PreAuthorize("hasRole('ROLE_find_topics_by_discipline')")
    @Operation(description = "Method that retrieve information about topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suceess|OK"),
    })
    @GetMapping("/discipline/{disciplineId}/topics")
    public List<Topic> findByDisciplineId(
            @PathVariable(value = "disciplineId") Long discipleId) {
        return topicService.findByDiscipline(discipleId);
    }

    @PreAuthorize("hasRole('ROLE_get_topic')")
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

    @PreAuthorize("hasRole('ROLE_find_tasks_by_topic')")
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
