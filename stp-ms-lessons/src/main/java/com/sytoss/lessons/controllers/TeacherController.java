package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.services.DisciplineService;
import com.sytoss.lessons.services.ExamService;
import com.sytoss.lessons.services.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final DisciplineService disciplineService;

    private final GroupService groupService;

    private final ExamService examService;

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

    @Operation(description = "Method that retrieve list of exams by teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/my/exams")
    public List<Exam> getMyExams() {
        return examService.findExams();
    }

    @Operation(description = "Method that retrieve list of exams by teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK")
    })
    @GetMapping("/my/exam/assignees")
    public List<ExamAssignee> getMyExamAssignees() {
        return examService.findExamAssignees();
    }
}