package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.ScheduleModel;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.lessons.services.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/assignee")
@RequiredArgsConstructor
public class ExamAssigneeController {

    private final ExamService examService;

    @Operation(description = "Method that reschedule exam by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @PostMapping(value = "/{examAssigneeId}/reschedule")
    public ExamAssignee reschedule(
            @Parameter(description = "id of exam to update")
            @PathVariable("examAssigneeId") Long examAssigneeId,
            @RequestBody ScheduleModel scheduleModel
    ) {
        return examService.reschedule(scheduleModel, examAssigneeId);
    }

    @Operation(description = "Method that returns all exam assignees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/{examId}/all")
    public List<ExamAssignee> getListOfExamAssignee(@PathVariable Long examId) {
        return examService.returnExamAssignees(examId);
    }

    @Operation(description = "Method that returns exam assignee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/{examAssigneeId}")
    public ExamAssignee getExamAssigneeById(@PathVariable Long examAssigneeId) {
        return examService.returnExamAssigneeById(examAssigneeId);
    }
}
