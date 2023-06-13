package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.services.DisciplineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final DisciplineService disciplineService;

    @PreAuthorize("hasRole('ROLE_create_discipline')")
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

    @PreAuthorize("hasRole('ROLE_find_discipline_by_teacher')")
    @Operation(description = "Method that retrieve disciplines by teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Teacher does not exist!"),
    })
    @GetMapping("/my/disciplines")
    public List<Discipline> findDisciplines() {
        return disciplineService.findDisciplines();
    }
}