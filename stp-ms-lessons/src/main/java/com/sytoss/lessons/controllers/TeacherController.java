package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.services.DisciplineService;
import com.sytoss.lessons.services.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final DisciplineService disciplineService;

    private final GroupService groupService;

    @Operation(description = "Method that register a new discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Discipline exists!"),
    })
    @PostMapping("/{teacherId}/discipline")
    public Discipline create(
            @Parameter(description = "id of teacher to be searched")
            @PathVariable("teacherId") Long teacherId,
            @RequestBody Discipline discipline) {
        return disciplineService.create(teacherId, discipline);
    }

    @Operation(description = "Method that retrieve disciplines by teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Teacher does not exist!"),
    })
    @GetMapping("/my/disciplines")
    public List<Discipline> findDisciplines() {
        return disciplineService.findDisciplines();
    }

    @Operation(description = "Method that retrieve disciplines by teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Teacher does not exist!"),
    })
    @GetMapping("/my/groups")
    public List<Group> getMyGroups() {
        return groupService.findGroups();
    }
}