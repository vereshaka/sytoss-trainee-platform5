package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.services.DisciplineService;
import com.sytoss.lessons.services.GroupService;
import com.sytoss.lessons.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/discipline")
@Tag(name = "DisciplineController")
public class DisciplineController {

    private final TopicService topicService;

    private final GroupService groupService;

    private final DisciplineService disciplineService;

    @Operation(description = "Method that register a new topic", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "409", description = "Topic exist!"),
    })
    @PostMapping("{disciplineId}/topic")
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
        return groupService.findByDiscipline(disciplineId);
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

    @Operation(description = "Method that create group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Discipline not found!"),
            @ApiResponse(responseCode = "409", description = "Group already exists!")
    })
    @PostMapping("/{disciplineId}/group")
    public Group createGroup(@Parameter(description = "id of the discipline to be searched by")
                             @PathVariable("disciplineId")
                             Long disciplineId,
                             @RequestBody Group group) {
        return groupService.create(disciplineId, group);
    }

}
