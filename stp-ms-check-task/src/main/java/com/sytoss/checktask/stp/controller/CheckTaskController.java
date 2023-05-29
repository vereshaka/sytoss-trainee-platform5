package com.sytoss.checktask.stp.controller;

import bom.CheckAnswerRequestBody;
import com.sytoss.checktask.stp.service.GradeService;
import com.sytoss.domain.bom.personalexam.Grade;
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

    @RequestMapping(method = RequestMethod.POST)
    public Grade check(
            @RequestBody CheckAnswerRequestBody body) {
        return gradeService.checkAndGrade(body);
    }
}
