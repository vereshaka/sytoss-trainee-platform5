package com.sytoss.users.controllers;

import com.sytoss.users.services.StudentService;
import com.sytoss.users.util.UpdatePhotoRequestParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(description = "Method that updates photo of student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Student not found!")
    })
    @PostMapping("/updatePhoto")
    public MultipartFile updatePhoto(@RequestBody UpdatePhotoRequestParams request) {
        return studentService.updatePhoto(request.getEmail(), request.getPhoto());
    }
}