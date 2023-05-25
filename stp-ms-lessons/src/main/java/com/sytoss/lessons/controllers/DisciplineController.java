package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discipline")
@Tag(name = "DisciplineController")
public class DisciplineController {

    @Autowired
    private TopicService topicService;

    @Operation(description = "Method that register a new topic", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Topic exist!"),
    })
    @PostMapping("{disciplineId}/topic")
    public Topic create(
            @Parameter(description = "id of discipline to be searched")
            @PathVariable("disciplineId") Long disciplineId,
            @RequestBody Topic topic) {
        return topicService.create(disciplineId, topic);
    }
}
