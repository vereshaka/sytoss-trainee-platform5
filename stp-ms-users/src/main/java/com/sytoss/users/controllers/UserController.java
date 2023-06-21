package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.users.model.ProfileModel;
import com.sytoss.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public AbstractUser me() {
        String email = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("email");
        return userService.getOrCreateUser(email);
    }

    @Operation(description = "Method that updates photo of student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/updatePhoto")
    public void updatePhoto(@RequestBody MultipartFile photo) {
        userService.updatePhoto(photo);
    }

    @Operation(description = "Update user profile info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping("/me")
    public void updateProfile(@RequestBody ProfileModel profileModel) {
        userService.updateProfile(profileModel);
    }
}
