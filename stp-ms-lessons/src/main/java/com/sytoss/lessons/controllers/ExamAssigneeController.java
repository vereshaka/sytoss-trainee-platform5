package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.ScheduleModel;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.lessons.ExamReportModel;
import com.sytoss.lessons.services.ExamAssigneeService;
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

    private final ExamAssigneeService examAssigneeService;

    @PreAuthorize("hasRole('Teacher')")
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

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that returns all exam assignees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/{examId}/all")
    public List<ExamAssignee> getListOfExamAssignee(@PathVariable Long examId) {
        return examService.returnExamAssignees(examId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that returns exam assignee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/{examAssigneeId}")
    public ExamAssignee getExamAssigneeById(@PathVariable Long examAssigneeId) {
        return examService.returnExamAssigneeById(examAssigneeId);
    }

    @Operation(description = "Method that create personal exams of group on student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @PostMapping("/group/{groupId}")
    public void createGroupExamsOnStudent(
            @Parameter(description = "Id of group to get exam assignees")
            @PathVariable("groupId") Long groupId,
            @RequestBody Student student
    ) {
        examService.createGroupExamsOnStudent(groupId, student);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that return exam info for excel report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Exam assignee not found")
    })
    @GetMapping(value = "/{examAssigneeId}/report")
    public ExamReportModel getReportInfo(
            @Parameter(description = "Id of exam assignee to get info by")
            @PathVariable Long examAssigneeId
    ) {
        return examService.getReportInfo(examAssigneeId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @GetMapping("/findByGroup/{groupId}")
    public List<ExamAssignee> getListOfExamAssigneeByGroup(@PathVariable Long groupId) {
        return examAssigneeService.findExamAssigneesByGroup(groupId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that deletes exam assignee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @DeleteMapping("/delete/{examAssigneeId}")
    public ExamAssignee deleteById(@PathVariable Long examAssigneeId) {
        return examAssigneeService.deleteById(examAssigneeId);
    }
}
