package com.sytoss.producer.controllers;

import com.sytoss.producer.bom.ExamConfiguration;
import com.sytoss.producer.bom.PersonalExam;
import com.sytoss.producer.services.PersonalExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PersonalExamController {

    @Autowired
    private PersonalExamService personalExamService;

    @Operation(description = "Method that create personal exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suceess|OK"),
    })
    @PostMapping("/personalExam/create")
    public PersonalExam createExam(@RequestBody ExamConfiguration examConfiguration) {
        return personalExamService.create(examConfiguration);
    }

}
