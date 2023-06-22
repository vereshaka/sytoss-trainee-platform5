package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.users.model.ProfileModel;
import com.sytoss.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

    @Operation(description = "Update user profile info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @PostMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateProfile(@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam MultipartFile photo) {
        ProfileModel profileModel = new ProfileModel(firstName, lastName, null, photo);
        userService.updatePhoto(profileModel.getPhoto());
        userService.updateProfile(profileModel);
    }
}
