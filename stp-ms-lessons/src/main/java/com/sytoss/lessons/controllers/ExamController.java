package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.ScheduleModel;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bom.ExamModel;
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
    public List<Exam> saveRequest(@RequestBody ExamModel exam) {
        return examService.save(exam.getExam(), exam.getGroups());
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

    @Operation(description = "Method that update exam by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @PostMapping(value = "/{examId}/reschedule")
    public Exam reschedule(
            @Parameter(description = "id of exam to update")
            @PathVariable("examId") Long examId,
            @RequestBody ScheduleModel scheduleModel
    ) {
        return examService.reschedule(scheduleModel, examId);
    }
}
