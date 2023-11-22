package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.analytics.Rating;
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
    public void updateAnalyticsElement(@RequestBody Analytics analytics) {
        analyticsService.updateAnalytic(analytics);
    }

    @Operation(description = "Method that migrate old tests to analytics elements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/migrate/{disciplineId}")
    public List<Analytics> migrate(@PathVariable Long disciplineId) {
        return analyticsService.migrate(disciplineId);
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
}
