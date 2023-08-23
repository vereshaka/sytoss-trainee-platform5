package com.sytoss.producer.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.domain.bom.personalexam.AnswerModule;
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
import org.springframework.http.ResponseEntity;
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

    @Operation(description = "Method that start exam for student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Exam is already started!")
    })
    @GetMapping("/{personalExamId}/start")
    public ResponseEntity<Question> start(
            @PathVariable("personalExamId")
            String personalExamId) {
        Question question = personalExamService.start(personalExamId);
        if (question != null) {
            return ResponseEntity.ok(question);
        }
        return ResponseEntity.accepted().body(null);
    }

    @Operation(description = "Method for answering tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/{personalExamId}/task/answer")
    public ResponseEntity<Question> answer(
            @Parameter(description = "id of personalExam to be searched")
            @PathVariable(value = "personalExamId") String personalExamId,
            @RequestBody AnswerModule answerModule
    ) {
        Question nextQuestion = answerService.answer(personalExamId, answerModule.getAnswer(), answerModule.getAnswerUIDate(), answerModule.getTimeSpent());
        if (nextQuestion != null) {
            return ResponseEntity.ok(nextQuestion);
        }
        return ResponseEntity.accepted().body(null);
    }

    @Operation(description = "Method for answering tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/{personalExamId}/task/check")
    public QueryResult check(
            @Parameter(description = "id of personalExam to be searched")
            @PathVariable(value = "personalExamId") String personalExamId,
            @RequestParam String taskAnswer) {
        return answerService.check(personalExamId, taskAnswer.replaceAll("\"", ""));
    }

    @Operation(description = "Method returns image of db structure for task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/{personalExamId}/task/dbStructure")
    public byte[] getDbStructureImage(
            @Parameter(description = "id of personalExam to be searched")
            @PathVariable(value = "personalExamId") String personalExamId) {
        return answerService.getDbImage(personalExamId);
    }

    @Operation(description = "Method returns image of db data for task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/{personalExamId}/task/dbData")
    public byte[] getDbDataImage(
            @Parameter(description = "id of personalExam to be searched")
            @PathVariable(value = "personalExamId") String personalExamId) {
        return answerService.getDataImage(personalExamId);
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
    @GetMapping("/student/{userId}")
    @JsonView(PersonalExam.Public.class)
    public List<PersonalExam> getByStudentId(@PathVariable(value = "userId") Long userId) {
        return personalExamService.getByStudentId(userId);
    }

    @Operation(description = "Method that return personal exams by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/teacher/{userId}")
    public List<PersonalExam> getByTeacherId(@PathVariable(value = "userId") Long userId) {
        return personalExamService.getByTeacherId(userId);
    }

    @Operation(description = "Method that change personal exam status to reviewed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/review")
    public PersonalExam review(@RequestBody PersonalExam personalExam) {
        return personalExamService.review(personalExam);
    }

    @Operation(description = "Method that retriev question image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/{personalExamId}/task/question")
    public byte[] getQuestionImage(@PathVariable("personalExamId") String personalExamId) {
        return personalExamService.getQuestionImage(personalExamId);
    }

    @Operation(description = "Method that update all personal exams by exam id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @PostMapping("/update")
    public List<PersonalExam> update(@RequestBody ExamConfiguration examConfiguration) {
        return personalExamService.update(examConfiguration);
    }
}
