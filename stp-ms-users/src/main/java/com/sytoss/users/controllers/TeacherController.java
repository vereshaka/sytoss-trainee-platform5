package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.services.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public AbstractUser me() {
        String email = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("email");
        return teacherService.getOrCreateUser(email);
    }
}
