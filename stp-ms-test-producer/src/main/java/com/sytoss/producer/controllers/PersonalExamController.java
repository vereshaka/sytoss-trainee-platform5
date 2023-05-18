package com.sytoss.producer.controllers;

import com.sytoss.producer.bom.Task;
import com.sytoss.producer.services.PersonalExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PersonalExamController {

    @Autowired
    private PersonalExamService personalExamService;

    @Operation(description = "Method that start exam for student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Exam is already started!")
    })
    @GetMapping ("/test/{personalExamId}/start")
    public Task start(
            @PathVariable("personalExamId")
            String personalExamId) {
        return personalExamService.start(personalExamId);
    }
}
