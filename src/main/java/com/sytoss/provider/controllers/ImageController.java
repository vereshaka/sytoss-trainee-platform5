package com.sytoss.provider.controllers;

import com.sytoss.provider.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(description = "Convert question into the question photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/api/save-image")
    public String saveImage(@RequestBody byte[] image) throws IOException {
        return imageService.saveImage(image);
    }

    @Operation(description = "Save image by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/public/api/image/{name}")
    public void saveImage(
            @RequestParam MultipartFile image,
            @Parameter(description = "Name of image to save")
            @PathVariable("name") String name) throws IOException {
        imageService.saveImageWithName(image, name);
    }

    @Operation(description = "Get image bytes by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("public/api/image/{name}")
    public byte[] getImage(
            @Parameter(description = "Name oof image to get")
            @PathVariable String name
    ) throws IOException {
        return imageService.getImageByName(name);
    }

    @Deprecated
    @Operation(description = "Save image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/public/api/image")
    public void saveImage(@RequestParam(required = false) MultipartFile image) throws IOException {
        imageService.saveImageWithName(image, image.getOriginalFilename());
    }
}
