package com.sytoss.producer.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.bom.AnswersModel;
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

import java.io.IOException;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/personal-exam")
public class PersonalExamController {

    @Autowired
    private PersonalExamService personalExamService;

    @Autowired
    private AnswerService answerService;

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that create personal exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/create")
    @JsonView({PersonalExam.Public.class})
    public PersonalExam createExam(@RequestBody ExamConfiguration examConfiguration) {
        return personalExamService.create(examConfiguration);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that return personal exam with summary grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @JsonView({PersonalExam.TeacherOnly.class})
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
    public QueryResult checkCurrentAnswer(
            @Parameter(description = "id of personalExam to be searched")
            @PathVariable(value = "personalExamId") String personalExamId,
            @RequestBody AnswersModel answersModel) {
        return answerService.checkCurrentAnswer(personalExamId, answersModel.getTaskAnswer(), answersModel.getCheckAnswer());
    }

    @Operation(description = "Method for answering tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/{personalExamId}/task/check/{answerId}")
    public QueryResult checkAnswerById(
            @Parameter(description = "id of personalExam to be searched")
            @PathVariable(value = "personalExamId") String personalExamId,
            @PathVariable(value = "answerId") String answerId,
            @RequestBody AnswersModel answersModel) {
        return answerService.checkByAnswerId(personalExamId, answersModel.getTaskAnswer(), answerId, answersModel.getCheckAnswer());
    }

    @Operation(description = "Method returns image of db structure for task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/{personalExamId}/task/dbStructure")
    public String getDbStructureImage(
            @Parameter(description = "id of personalExam to be searched")
            @PathVariable(value = "personalExamId") String personalExamId) {
        return answerService.getDbImage(personalExamId);
    }

    @Operation(description = "Method returns image of db data for task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/{personalExamId}/task/dbData")
    public String getDbDataImage(
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

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that return personal exams by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/teacher/{userId}")
    @JsonView(PersonalExam.Public.class)
    public List<PersonalExam> getByTeacherId(@PathVariable(value = "userId") Long userId) {
        return personalExamService.getByTeacherId(userId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that change personal exam status to reviewed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/review")
    @JsonView(PersonalExam.TeacherOnly.class)
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

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that update all personal exams by exam id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @PostMapping("/reschedule")
    @JsonView(PersonalExam.Public.class)
    public List<PersonalExam> reschedule(@RequestBody ExamConfiguration examConfiguration) {
        return personalExamService.reschedule(examConfiguration);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that retrieve personal exams by exam id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/exam/assignee/{examAssigneeId}")
    @JsonView(PersonalExam.Public.class)
    public List<PersonalExam> getByExamAssigneeId(
            @Parameter(description = "Id of exam to get personal exams")
            @PathVariable("examAssigneeId") Long examAssigneeId) {
        return personalExamService.getByExamAssigneeId(examAssigneeId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that delete personal exam by exam id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @DeleteMapping(value = "/exam/assignee/{examAssigneeId}")
    @JsonView(PersonalExam.Public.class)
    public List<PersonalExam> deleteByExamId(
            @Parameter(description = "Id of exam to delete personal exams")
            @PathVariable("examAssigneeId") Long examAssigneeId) {
        return personalExamService.deleteByExamAssigneeId(examAssigneeId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that delete personal exams by exam assignee ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @DeleteMapping(value = "/exam/delete")
    @JsonView(PersonalExam.Public.class)
    public List<PersonalExam> deleteByExamAssigneeIds(
            @Parameter(description = "Exam assignee ids to delete personal exams")
            @RequestBody List<Long> examAssigneeIds
    ) {
        return personalExamService.deleteByExamAssigneeIds(examAssigneeIds);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that change personal exam status to reviewed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/review/answers")
    public ExamAssigneeAnswersModel reviewAnswers(@RequestBody ExamAssigneeAnswersModel answers) {
        return personalExamService.reviewByAnswers(answers);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that return excel file of personal exams by exam assignee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping(value = "/excel/assignee/{examAssigneeId}")
    public byte[] getExcelReport(
            @Parameter(description = "Id of exam assignee to get excel report about personal exams")
            @PathVariable Long examAssigneeId
    ) throws IOException {
        return personalExamService.getExcelReport(examAssigneeId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that return excel file of personal exam by group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping(value = "/excel/group/{groupId}")
    public byte[] getExcelReportByGroup(
            @Parameter(description = "Id of group to get excel report about personal exams")
            @PathVariable Long groupId
    ) throws IOException {
        return personalExamService.getExcelReportByGroup(groupId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that change personal exam status to reviewed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/task/update")
    public List<PersonalExam> updateTask(@RequestBody Task task) {
        return personalExamService.updateTask(task);
    }


    @Operation(description = "Method for making user screenshot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/{personalExamId}/screenshot")
    public void makeUserScreenshot(
            @Parameter(description = "id of personalExam to be searched")
            @PathVariable(value = "personalExamId") String personalExamId,
            @RequestBody String capture
    ) {
        personalExamService.makeScreenshot(capture, personalExamId);
    }

    @Operation(description = "Method to get list of personal exams by examId and studentId for migration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/migrate")
    public List<PersonalExam> getListOfPersonalExamsByExamIdAndStudentId(Long examAssigneeId,Long studentId) {
        return personalExamService.getListOfPersonalExamByExamIdAndStudentId(examAssigneeId, studentId);
    }
}