package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.services.DisciplineService;
import com.sytoss.lessons.services.TaskDomainService;
import com.sytoss.lessons.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/")
    public List<Discipline> findAllDisciplines() {
        return disciplineService.findAllDisciplines();
    }


    @Operation(description = "Method that retrieve list of my disciplines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/me")
    public List<Discipline> findAllMyDisciplines() {
        return disciplineService.findAllMyDiscipline();
    }

}