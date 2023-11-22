package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.analytics.Analytic;
import com.sytoss.lessons.services.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('Teacher')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analytics")
@Tag(name = "AnalyticsController")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(description = "Method that update information about student's analytics element")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("")
    public void updateAnalyticsElement(@RequestBody Analytic analytic) {
        analyticsService.updateAnalytic(analytic);
    }

    @Operation(description = "Method that migrate old tests to analytics elements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/migrate/{disciplineId}")
    public List<Analytic> migrate(@PathVariable Long disciplineId) {
        return analyticsService.migrate(disciplineId);
    }
}
