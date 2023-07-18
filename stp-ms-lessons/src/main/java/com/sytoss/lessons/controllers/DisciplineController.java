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
@RequestMapping("/api/discipline")
@Tag(name = "DisciplineController")
public class DisciplineController {

    private final TopicService topicService;

    private final DisciplineService disciplineService;

    private final TaskDomainService taskDomainService;

    @Operation(description = "Method that register a new discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Discipline exists!"),
    })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Discipline create(
            @RequestParam String name,
            @RequestParam(required = false) String shortDescription,
            @RequestParam(required = false) String fullDescription,
            @RequestParam(required = false) byte[] icon
    ) {
        Discipline request = new Discipline();
        request.setName(name);
        request.setShortDescription(shortDescription);
        request.setFullDescription(fullDescription);
        request.setIcon(icon);
        return disciplineService.create(request);
    }

    @Operation(description = "Method that register a new topic", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Topic exist!"),
    })
    @PostMapping("/{disciplineId}/topic")
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
    @GetMapping("/{disciplineId}/groups")
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
    @GetMapping("/{disciplineId}")
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
    @PostMapping("/{disciplineId}/task-domain")
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
    @GetMapping("/{disciplineId}/task-domains")
    public List<TaskDomain> findTasksDomainByDiscipline(
            @Parameter(description = "id of the discipline to be searched by")
            @PathVariable("disciplineId") Long disciplineId) {
        return taskDomainService.findByDiscipline(disciplineId);
    }

    @Operation(description = "Method that join group to discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Group Not Found!")
    })
    @PostMapping("/{disciplineId}/group/{groupId}")
    public void assignGroup(
            @Parameter(description = "id of the discipline to join")
            @PathVariable("disciplineId") Long disciplineId,
            @Parameter(description = "id of the group to join discipline")
            @PathVariable("groupId") Long groupId) {
        disciplineService.assignGroupToDiscipline(disciplineId, groupId);
    }

    @Operation(description = "Method that retrieve discipline's icon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Discipline not found!")
    })
    @GetMapping(value = "/{disciplineId}/icon", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getIcon(@Parameter(description = "id of the discipline to search icon")
                                        @PathVariable("disciplineId")
                                        Long disciplineId) {
        return disciplineService.getIcon(disciplineId);
    }

    @Operation(description = "Method that retrieve information about topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suceess|OK"),
    })
    @GetMapping("/{disciplineId}/topics")
    public List<Topic> findByDisciplineId(
            @PathVariable(value = "disciplineId") Long discipleId) {
        return topicService.findByDiscipline(discipleId);
    }
}