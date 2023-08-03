package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
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

import java.util.List;

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
    public void updateProfile(@RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String middleName,
                              @RequestParam(required = false) String lastName,
                              @RequestParam(required = false) MultipartFile photo) {
        ProfileModel profileModel = new ProfileModel(firstName, middleName, lastName, null, photo);
        userService.updateProfile(profileModel);
    }

    @Operation(description = "Get groups by student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/me/groups")
    public List<Group> findGroupByStudent() {
        return userService.findByStudent();
    }

    @Operation(description = "Receive user's photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "User not found!")
    })
    @GetMapping(value = "/{userId}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getUserPhoto(@PathVariable("userId") String userId) {
        return userService.getUserPhoto(userId);
    }

    @Operation(description = "Method that retrieve user's photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Photo not found!")
    })
    @GetMapping(value = "/me/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getMyPhoto() {
        return userService.getMyPhoto();
    }

    @Operation(description = "find my groups Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/me/groupsId")
    public List<Long> findDisciplineByStudent() {
        return userService.findGroupsId();
    }
}
