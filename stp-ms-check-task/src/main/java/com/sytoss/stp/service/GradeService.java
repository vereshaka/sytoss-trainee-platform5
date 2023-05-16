package com.sytoss.stp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeService {

    @Autowired
    private DatabaseHelperService databaseHelperService;

    public void checkAndGrade() throws Exception {
        String sqlAnswer = "select * from Person";
        String sqlEtalon = "select * from Person";
        databaseHelperService.generateDatabase();
        databaseHelperService.executeQuery(sqlAnswer);
        databaseHelperService.executeQuery(sqlEtalon);
        databaseHelperService.dropDatabase();
    }
}
