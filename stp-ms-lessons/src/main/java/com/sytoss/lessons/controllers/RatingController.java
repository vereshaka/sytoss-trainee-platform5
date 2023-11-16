package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Rating;
import com.sytoss.lessons.services.RatingService;
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
@RequestMapping("/api/rating")
@Tag(name = "RatingController")
public class RatingController {

    private final RatingService ratingService;

    @Operation(description = "Method that update information about student's rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/update")
    public Rating updateRating(@RequestBody Rating rating) {
        return ratingService.updateRating(rating);
    }

    @Operation(description = "Method that migrate old tests to init ratings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/migrate/{disciplineId}")
    public List<Rating> migrate(@PathVariable Long disciplineId) {
        return ratingService.migrate(disciplineId);
    }
}