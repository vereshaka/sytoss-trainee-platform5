package com.sytoss.checktask.stp.controller;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.checktask.stp.service.GradeService;
import com.sytoss.domain.bom.personalexam.Grade;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/task/check")
public class CheckTaskController {

    private final GradeService gradeService;

    @ApiResponse(responseCode = "200", description = "Success|OK")
    @RequestMapping(method = RequestMethod.POST)
    public Grade check(
            @RequestBody CheckTaskParameters body) {
        return gradeService.checkAndGrade(body);
    }
}
