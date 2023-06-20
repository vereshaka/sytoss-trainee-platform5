package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.users.services.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService  groupService;

    @Operation(description = "Method that create group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Discipline not found!"),
            @ApiResponse(responseCode = "409", description = "Group already exists!")
    })
    @PostMapping("/")
    public Group createGroup(
             @RequestBody Group group) {
        return groupService.create(group);
    }

}
