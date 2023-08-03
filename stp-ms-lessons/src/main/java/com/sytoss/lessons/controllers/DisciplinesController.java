package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.services.DisciplineService;
import com.sytoss.lessons.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/disciplines")
@Tag(name = "DisciplineController")
public class DisciplinesController {

    private final TopicService topicService;

    private final DisciplineService disciplineService;

    @Operation(description = "Method that retrieve list of disciplines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("")
    public List<Discipline> findAllDisciplines() {
        return disciplineService.findAllDisciplines();
    }


    @Operation(description = "Method that retrieve list of my disciplines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/my")
    public List<Discipline> findAllMyDisciplines() {
        return disciplineService.findAllMyDiscipline();
    }

    @Operation(description = "Method that retrieve list tasks by discipline id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/{id}/tasks")
    public List<Task> findTasksByDisciplineId(@PathVariable Long id) {
        return disciplineService.findTasksByDisciplineId(id);
    }

}