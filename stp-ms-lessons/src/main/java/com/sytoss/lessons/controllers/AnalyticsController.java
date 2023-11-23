package com.sytoss.lessons.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.analytics.Analytic;
import com.sytoss.domain.bom.analytics.AnalyticFull;
import com.sytoss.lessons.services.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void updateAnalyticsElement(@RequestBody Analytic analytic) {
        analyticsService.updateAnalytic(analytic);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that migrate old tests to analytics elements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/migrate/{disciplineId}")
    public List<Analytic> migrate(@PathVariable Long disciplineId) {
        return analyticsService.migrate(disciplineId);
    }

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method returns analytics for teacher about certain student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @JsonView({AnalyticFull.AnalyticFullView.class})
    @GetMapping("/discipline/{disciplineId}/student/{studentId}")
    public AnalyticFull getStudentAnalytics(@PathVariable Long disciplineId, @PathVariable Long studentId){
        return analyticsService.getStudentAnalyticsByStudentId(disciplineId, studentId);
    }

    @Operation(description = "Method returns analytics for currently logged student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @JsonView({AnalyticFull.AnalyticFullView.class})
    @GetMapping("/discipline/{disciplineId}/student")
    public AnalyticFull getStudentAnalytics(@PathVariable Long disciplineId){
        return analyticsService.getStudentAnalyticsByLoggedStudent(disciplineId);
    }

}
