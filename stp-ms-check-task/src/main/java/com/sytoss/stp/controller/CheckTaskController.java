package com.sytoss.stp.controller;

import com.sytoss.stp.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/task/check/{answer}/{etalon}")
public class CheckTaskController {

    private final GradeService gradeService;

    @RequestMapping(method = RequestMethod.POST)
    public void checkTask(@PathVariable String answer,
                          @PathVariable String etalon,
                          @RequestBody String databaseScript) throws Exception {
        gradeService.checkAndGrade(answer,etalon,databaseScript);
    }
}
