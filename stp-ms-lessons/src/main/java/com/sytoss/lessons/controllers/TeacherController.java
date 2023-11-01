package com.sytoss.lessons.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.controllers.api.FilterItem;
import com.sytoss.lessons.controllers.api.PagingInfo;
import com.sytoss.lessons.controllers.api.ResponseObject;
import com.sytoss.lessons.controllers.filter.FilterFactory;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.services.DisciplineService;
import com.sytoss.lessons.services.ExamService;
import com.sytoss.lessons.services.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('Teacher')")
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
    @PostMapping("/my/disciplines/{page}/{pageSize}")
    public ResponseObject findDisciplines(@PathVariable int page,
                                          @PathVariable int pageSize,
                                          @RequestBody(required = false) List<FilterItem> filters
    ) {
        ResponseObject<Discipline> responseObject = new ResponseObject();
        if (CollectionUtils.isEmpty(filters)){
            filters = FilterFactory.getFilterSet(DisciplineDTO.class);
        }
        Page<Discipline> disciplines = disciplineService.findDisciplinesWithPaging(page, pageSize, filters);
        responseObject.setData(disciplines.getContent());
        responseObject.setPaging(new PagingInfo(disciplines.getTotalElements(), page, pageSize));

        responseObject.setFilters(filters);
        return responseObject;
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


    @JsonView({Group.TeacherGroups.class})
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