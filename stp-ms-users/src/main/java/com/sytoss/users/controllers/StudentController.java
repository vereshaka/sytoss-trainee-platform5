package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.services.StudentService;
import com.sytoss.users.services.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(description = "Method that updates photo of student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/updatePhoto")
    public ResponseEntity<String> create(@RequestBody String email,
                                 @RequestBody MultipartFile photo) throws IOException {
        return studentService.updatePhoto(email, photo);
    }
}