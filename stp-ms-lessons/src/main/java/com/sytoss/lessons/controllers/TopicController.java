package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TopicController {

    private final TopicService topicService;

    @Operation(description = "Method that retrieve information about topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suceess|OK"),
    })
    @GetMapping("/discipline/{disciplineId}/topics")
    public List<Topic> findByDisciplineId(@PathVariable(value = "disciplineId") Long discipleId) {
        return topicService.findByDiscipline(discipleId);
    }
}