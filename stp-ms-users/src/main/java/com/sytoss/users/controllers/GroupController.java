package com.sytoss.users.controllers;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.services.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @Operation(description = "Method that create group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Discipline not found!"),
            @ApiResponse(responseCode = "409", description = "Group already exists!")
    })
    @PostMapping("")
    public Group createGroup(
            @RequestBody Group group) {
        return groupService.create(group);
    }

    @Operation(description = "Method that assign student to group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Group not found!"),
    })
    @PostMapping("/{groupId}/student/{studentId}")
    public void assignStudent(
            @Parameter(description = "Id of group what will be has student")
            @PathVariable("groupId") Long groupId,
            @Parameter(description = "Id of student what will be assignee to group")
            @PathVariable("studentId") String studentId) {
        groupService.assignStudentToGroup(groupId, studentId);
    }

    @Operation(description = "Method that get information about group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Group not found!"),
    })
    @GetMapping("/{groupId}")
    public Group getGroup(
            @Parameter(description = "Id of group")
            @PathVariable("groupId") Long groupId) {
        return groupService.getById(groupId);
    }

    @Operation(description = "Method that get information about my groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Group not found!"),
    })
    @GetMapping("/{groupId}/students")
    public List<Student> getStudentsByGroup(
            @Parameter(description = "Id of group")
            @PathVariable("groupId") Long groupId) {
        return groupService.getStudents(groupId);
    }
}
