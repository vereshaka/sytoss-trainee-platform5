package com.sytoss.producer.controllers;

import com.sytoss.producer.services.RatingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingsService ratingsService;

    @PreAuthorize("hasRole('Teacher')")
    @Operation(description = "Method that get average estimate for group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/{examId}/average/estimate/{groupId}")
    public Double getAverageEstimation(@PathVariable Long examId,
                                       @PathVariable Long groupId) {
        return ratingsService.getAverageEstimation(examId,groupId);
    }

}
