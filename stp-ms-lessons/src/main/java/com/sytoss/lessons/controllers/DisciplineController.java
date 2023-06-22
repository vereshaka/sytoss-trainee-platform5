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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "DisciplineController")
public class DisciplineController {

    private final TopicService topicService;

    private final DisciplineService disciplineService;

    private final TaskDomainService taskDomainService;

    @Operation(description = "Method that register a new topic", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Topic exist!"),
    })
    @PostMapping("/discipline/{disciplineId}/topic")
    public Topic create(
            @Parameter(description = "id of discipline to be searched")
            @PathVariable("disciplineId") Long disciplineId,
            @RequestBody Topic topic) {
        return topicService.create(disciplineId, topic);
    }

    @Operation(description = "Method that retrieve groups by discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/discipline/{disciplineId}/groups")
    public List<Group> findByDiscipline(@Parameter(description = "id of the discipline to be searched by")
                                        @PathVariable("disciplineId")
                                        Long disciplineId) {
        return disciplineService.getGroups(disciplineId);
    }

    @Operation(description = "Method that retrieve discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Discipline not found!")
    })
    @GetMapping("/discipline/{disciplineId}")
    public Discipline getDiscipline(@Parameter(description = "id of the discipline to be searched by")
                                    @PathVariable("disciplineId")
                                    Long disciplineId) {
        return disciplineService.getById(disciplineId);
    }

    @Operation(description = "Method that create a new task domain")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Task domain already exist")
    })
    @PostMapping("/discipline/{disciplineId}")
    public TaskDomain create(
            @Parameter(description = "id of the discipline to be searched by")
            @PathVariable("disciplineId") Long disciplineId,
            @RequestBody TaskDomain taskDomain) {
        return taskDomainService.create(disciplineId, taskDomain);
    }

    @Operation(description = "Method that retrieve all task domains by discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/discipline/{disciplineId}/taskDomains")
    public List<TaskDomain> findTasksDomainByDiscipline(
            @Parameter(description = "id of the discipline to be searched by")
            @PathVariable("disciplineId") Long disciplineId) {
        return taskDomainService.findByDiscipline(disciplineId);
    }

    @Operation(description = "Method that retrieve list of disciplines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping("/disciplines")
    public List<Discipline> findAllDisciplines() {
        return disciplineService.findAllDisciplines();
    }

    @Operation(description = "Method that join group to discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Group Not Found!")
    })
    @PostMapping("/discipline/{disciplineId}/group/{groupId}")
    public void assignGroup(
            @Parameter(description = "id of the discipline to join")
            @PathVariable("disciplineId") Long disciplineId,
            @Parameter(description = "id of the group to join discipline")
            @PathVariable("groupId") Long groupId) {
        disciplineService.assignGroupToDiscipline(disciplineId, groupId);
    }
}