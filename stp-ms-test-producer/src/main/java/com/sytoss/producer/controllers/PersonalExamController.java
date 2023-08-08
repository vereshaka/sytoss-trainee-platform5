package com.sytoss.producer.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.producer.connectors.PersonalExamConnector;
import com.sytoss.producer.services.AnswerService;
import com.sytoss.producer.services.PersonalExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/personal-exam")
public class PersonalExamController {

    @Autowired
    private PersonalExamService personalExamService;

    @Autowired
    private PersonalExamConnector personalExamConnector;

    @Autowired
    private AnswerService answerService;

    @Operation(description = "Method that create personal exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/create")
    public PersonalExam createExam(@RequestBody ExamConfiguration examConfiguration) {
        return personalExamService.create(examConfiguration);
    }

    @Operation(description = "Method that return personal exam with summary grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @JsonView(PersonalExam.Public.class)
    @GetMapping("/{id}/summary")
    public PersonalExam summary(@PathVariable(value = "id") String examId) {
        return personalExamService.summary(examId);
    }


    @Operation(description = "Method for answering tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/{personalExamId}/task/answer")
    public Answer answer(
            @Parameter(description = "id of personalExam to be searched")
            @PathVariable(value = "personalExamId") String personalExamId,
            @RequestBody String taskAnswer) {
        return answerService.answer(personalExamId, taskAnswer);
    }


    @Operation(description = "Method that start exam for student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Exam is already started!")
    })
    @GetMapping("/{personalExamId}/start")
    public Question start(
            @PathVariable("personalExamId")
            String personalExamId) {
        return personalExamService.start(personalExamId);
    }


    @Operation(description = "Method that return personal exam by task domain id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/is-used-now/task-domain/{taskDomainId}")
    public boolean taskDomainIsUsed(@PathVariable(value = "taskDomainId") Long taskDomainId) {
        return personalExamService.taskDomainIsUsed(taskDomainId);
    }

    @Operation(description = "Method that return personal exams by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/user/{userId}")
    public List<PersonalExam> getByUserId(@PathVariable(value = "userId") Long userId) {
        return personalExamService.getByUserId(userId);
    }
}
