package com.sytoss.provider.controller;

import com.sytoss.provider.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @Operation(description = "Convert question into the question photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/convert/image")
    public Long convertImage(@RequestBody String question) {
        return imageService.generatePngFromQuestion(question);
    }

    @Operation(description = "Get image by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    @GetMapping("/question-image/{question_image_id}")
    public byte[] getImage(@PathVariable("question_image_id") Long id) {
        return imageService.getById(id);
    }
}
