package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bom.TaskIds;
import com.sytoss.lessons.services.TaskService;
import com.sytoss.lessons.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/topic")
public class TopicController {

    private final TopicService topicService;

    private final TaskService taskService;

    @Operation(description = "Method that return topic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suceess|OK"),
            @ApiResponse(responseCode = "404", description = "Discipline not found!"),
    })
    @GetMapping("/{topicId}")
    public Topic getById(
            @Parameter(description = "id of topic to be searched")
            @PathVariable(value = "topicId")
            Long topicId) {
        return topicService.getById(topicId);
    }

    @Operation(description = "Method that assigns tasks to topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task not found!"),
            @ApiResponse(responseCode = "404", description = "Topic not found!")
    })
    @PostMapping("/{topicId}/assign/tasks")
    public List<Task> assignTaskToTopic(@Parameter(description = "id of the task to be searched by")
                                        @PathVariable("topicId") Long topicId,
                                        @Parameter(description = "id of the task to be searched by")
                                        @RequestBody TaskIds taskIds) {
        return taskService.assignTasksToTopic(topicId, taskIds.getTaskIds());
    }

    @Operation(description = "Method that gets tasks by topic ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success|OK"),
                    @ApiResponse(responseCode = "404", description = "Topic not found")
            })
    @GetMapping("/{topicId}/tasks")
    public List<Task> findByTopicId(
            @PathVariable(value = "topicId") Long topicId) {
        return taskService.findByTopicId(topicId);
    }

    @Operation(description = "Method that retrieve topic's icon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Topic not found!")
    })
    @GetMapping(value = "/{topicId}/icon", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getIcon(@Parameter(description = "id of the topic to search icon")
                                        @PathVariable("topicId")
                                        Long topicId) {
        return topicService.getIcon(topicId);
    }

    @Operation(description = "Method that update topic by discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Topic not found!")
    })
    @PutMapping(value = "/{topicId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Topic update(
            @Parameter(description = "Id of topic to update")
            @PathVariable("topicId") Long topicId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String shortDescription,
            @RequestParam(required = false) String fullDescription,
            @RequestParam(required = false) Double duration,
            @RequestParam(required = false) MultipartFile icon
    ) throws IOException {
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setName(name);
        topic.setDuration(duration);
        topic.setFullDescription(fullDescription);
        topic.setShortDescription(shortDescription);

        if (Objects.nonNull(icon)) {
            topic.setIcon(icon.getBytes());
        }

        return topicService.update(topic);
    }
}
