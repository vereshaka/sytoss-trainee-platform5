package com.sytoss.stp.controller;

import com.sytoss.stp.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/task/check")
public class CheckTaskController {

    @Autowired
    private GradeService gradeService;

    @RequestMapping(method = RequestMethod.POST)
    public void checkTask() throws Exception {
        gradeService.checkAndGrade();
    }
}
