package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.AnalyticsElement;
import com.sytoss.domain.bom.lessons.analytics.RatingModel;
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
    @PostMapping("/update")
    public AnalyticsElement updateAnalyticsElement(@RequestBody AnalyticsElement analyticsElement) {
        return analyticsService.updateAnalyticsElement(analyticsElement);
    }

    @Operation(description = "Method that migrate old tests to analytics elements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/migrate/{disciplineId}")
    public List<AnalyticsElement> migrate(@PathVariable Long disciplineId) {
        return analyticsService.migrate(disciplineId);
    }

    @Operation(description = "Method that retrieves analytics elements by discipline id, group id, exam id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/rating/discipline/{disciplineId}/group/{groupId}/exam/{examId}")
    public List<RatingModel> returnAnalyticsElementsByDisciplineGroupExam(@PathVariable Long disciplineId,
                                                                          @PathVariable(name = "groupId", required = false) String groupStringId,
                                                                          @PathVariable(name = "examId", required = false) String examStringId) {
        Long examId=null, groupId=null;
        if(!Objects.equals(groupStringId, "null")){
            groupId = Long.parseLong(groupStringId);
        }
        if(!Objects.equals(examStringId, "null")){
            examId = Long.parseLong(examStringId);
        }
        return analyticsService.getAnalyticsElementsByDisciplineGroupExam(disciplineId,groupId,examId);
    }
}