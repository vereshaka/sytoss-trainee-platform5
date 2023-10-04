package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.ScheduleModel;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.lessons.examassignee.ExamGroupAssignee;
import com.sytoss.domain.bom.lessons.examassignee.ExamStudentAssignee;
import com.sytoss.lessons.services.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @Operation(description = "Method that save information about exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/save")
    public Exam save(@RequestBody Exam exam) {
        return examService.save(exam);
    }


    @Operation(description = "Method that retrieve exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Exam not found!")
    })
    @GetMapping("/{examId}")
    public Exam getById(@Parameter(description = "id of the exam to be searched by")
                        @PathVariable("examId")
                        Long examId) {
        return examService.getById(examId);
    }

    @Operation(description = "Method that retrieve information about topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suceess|OK"),
    })
    @GetMapping("/{examId}/topics")
    public List<Topic> findTopicsByExamId(
            @PathVariable(value = "examId") Long examId) {
        Exam exam = examService.getById(examId);
        return ListUtils.emptyIfNull(exam.getTopics());
    }

    @Operation(description = "Method that retrieve information about tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suceess|OK"),
    })
    @GetMapping("/{examId}/tasks")
    public List<Task> findTasksByExamId(
            @PathVariable(value = "examId") Long examId) {
        Exam exam = examService.getById(examId);
        return ListUtils.emptyIfNull(exam.getTasks());
    }

    @Operation(description = "Method that delete exam by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Exam not found!")

    })
    @DeleteMapping(value = "/{examId}/delete")
    public Exam deleteById(
            @Parameter(description = "Id of exam to delete")
            @PathVariable("examId") Long examId
    ) {
        return examService.delete(examId);
    }

    @Operation(description = "Method that assign exam to group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/assign/{examId}/groups")
    public Exam assignGroupsToExam(@PathVariable Long examId, @RequestBody ExamGroupAssignee examAssignee) {
        return examService.assignExamForGroup(examId, examAssignee);
    }

    @Operation(description = "Method that assign exam to group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/assign/{examId}/students")
    public Exam assignStudentToExam(@PathVariable Long examId, @RequestBody ExamStudentAssignee examAssignee) {
        return examService.assignExamForStudents(examId, examAssignee);
    }
}
