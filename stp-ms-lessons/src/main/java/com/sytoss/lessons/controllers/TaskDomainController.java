package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bom.TaskDomainModel;
import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import com.sytoss.lessons.services.TaskDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/task-domain")
@RequiredArgsConstructor
public class TaskDomainController {

    private final TaskDomainService taskDomainService;

    @Operation(description = "Method that save information about exam")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task domain not found"),
    })
    @GetMapping("/{taskDomainId}")
    public TaskDomain getById(@Parameter(description = "id of the task domain to be searched by")
                              @PathVariable(value = "taskDomainId") Long taskDomainId) {
        return taskDomainService.getById(taskDomainId);
    }

    @Operation(description = "Method that update task domain")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task domain not found"),
            @ApiResponse(responseCode = "409", description = "Task domain is used"),
    })
    @PutMapping("/{taskDomainId}")
    public TaskDomain update(@Parameter(description = "id of the task domain to be searched by")
                             @PathVariable(value = "taskDomainId") Long taskDomainId,
                             @RequestBody TaskDomain taskDomain) {
        return taskDomainService.update(taskDomainId, taskDomain);
    }

    @Operation(description = "Method generate image from puml", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @PutMapping("/puml/{dataParameter}")
    public byte[] generatePngFromPuml(@Parameter(description = "id of the task domain to be searched by")
                                      @PathVariable(name = "dataParameter") ConvertToPumlParameters parameter,
                                      @RequestBody (required = false) String puml) {
        return taskDomainService.generatePngFromPuml(puml, parameter);
    }

    @Operation(description = "Method get count of tasks by task domain", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/{taskDomainId}/overview")
    public TaskDomainModel getCountOfTasks(@Parameter(description = "id of the task domain to be searched by")
                                           @PathVariable(value = "taskDomainId") Long taskDomainId) {
        return taskDomainService.getCountOfTasks(taskDomainId);
    }


    @Operation(description = "Method get tasks by task domain", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/{taskDomainId}/tasks")
    public List<Task> getTasks(@Parameter(description = "id of the task domain to be searched by")
                                           @PathVariable(value = "taskDomainId") Long taskDomainId) {
        return taskDomainService.getTasks(taskDomainId);
    }

    @Operation(description = "Method that update task domain bu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @PostMapping("/{taskDomainId}/update")
    public TaskDomain updateById(
            @Parameter(description = "id of task domain to update")
            @PathVariable("taskDomainId") Long taskDomainId,
            @RequestBody TaskDomain taskDomain
    ) {
        taskDomain.setId(taskDomainId);
        return taskDomainService.updateById(taskDomain);
    }

    @Operation(description = "Method that delete task domain by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task domain not found!")
    })
    @DeleteMapping("/{taskDomainId}/delete")
    public TaskDomain delete(
            @Parameter(description = "Id of task domain to delete")
            @PathVariable("taskDomainId") Long taskDomainId
    ) {
        return taskDomainService.delete(taskDomainId);
    }

    @Operation(description = "Method that retrieve exams by task domain id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
            @ApiResponse(responseCode = "404", description = "Task domain not found")
    })
    @GetMapping("/{taskDomainId}/exams")
    public List<Exam> getExams(
            @Parameter(description = "Id of task domain to get exams")
            @PathVariable("taskDomainId") Long taskDomainId
    ) {
        return taskDomainService.getExams(taskDomainId);
    }
}
