package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.analytics.Rating;
import com.sytoss.lessons.controllers.viewModel.DisciplineSummary;
import com.sytoss.lessons.controllers.viewModel.StudentDisciplineStatistic;
import com.sytoss.lessons.services.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analytics")
@Tag(name = "AnalyticsController")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that update information about student's analytics element")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("")
    public void updateAnalyticsElement(@RequestBody Analytics analytics) {
        analyticsService.updateAnalytic(analytics);
    }

    @Operation(description = "Method that migrate old tests to analytics elements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/migrate/all")
    public void migrateAll() {
        analyticsService.migrateAll();
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that migrate old tests to analytics elements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/migrate/{disciplineId}")
    public void migrate(@PathVariable Long disciplineId) {
        analyticsService.migrate(disciplineId);
    }

    @Operation(description = "Method that retrieves analytics elements by discipline id, group id, exam id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/rating/discipline/{disciplineId}/group/{groupId}/exam/{examId}")
    public List<Rating> returnAnalyticsElementsByDisciplineGroupExam(@PathVariable Long disciplineId,
                                                                     @PathVariable(name = "groupId", required = false) String groupStringId,
                                                                     @PathVariable(name = "examId", required = false) String examStringId) {
        Long examId = null, groupId = null;
        if (!Objects.equals(groupStringId, "null")) {
            groupId = Long.parseLong(groupStringId);
        }
        if (!Objects.equals(examStringId, "null")) {
            examId = Long.parseLong(examStringId);
        }
        return analyticsService.getAnalyticsElementsByDisciplineGroupExam(disciplineId, groupId, examId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method returns analytics for teacher about certain student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/discipline/{disciplineId}/student/{studentId}")
    public StudentDisciplineStatistic getStudentAnalyticsForTeacher(@PathVariable Long disciplineId, @PathVariable Long studentId){
        return analyticsService.getStudentAnalyticsByStudentId(disciplineId, studentId);
    }

    @PreAuthorize("hasRole('Student')")
    @Operation(description = "Method returns analytics for currently logged student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/discipline/{disciplineId}/student")
    public StudentDisciplineStatistic getStudentAnalyticsForStudent(@PathVariable Long disciplineId){
        return analyticsService.getStudentAnalyticsByLoggedStudent(disciplineId);
    }

    @PreAuthorize("hasRole('Student')")
    @Operation(description = "Method returns statistics for currently logged student by group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/discipline/{disciplineId}/student/group/primary")
    public DisciplineSummary getStudentAnalyticsByGroup(@PathVariable Long disciplineId){
        return analyticsService.getDisciplineSummaryByGroupForStudent(disciplineId);
    }

    @Operation(description = "Method returns summary for discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/discipline/{disciplineId}/summary")
    public DisciplineSummary getDisciplineSummary(@PathVariable Long disciplineId){
        return analyticsService.getDisciplineSummary(disciplineId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method returns summary for discipline by certain group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/discipline/{disciplineId}/summary/group/{groupId}")
    public DisciplineSummary getDisciplineSummaryByGroup(@PathVariable Long disciplineId, @PathVariable Long groupId){
        return analyticsService.getDisciplineSummaryByGroup(disciplineId, groupId);
    }

}
