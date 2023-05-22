package com.sytoss.stp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final DatabaseHelperService databaseHelperService;

    public void checkAndGrade(String answer, String etalon, String databaseScript) throws Exception {
        databaseHelperService.generateDatabase(databaseScript);
        databaseHelperService.getExecuteQueryResult(answer,etalon);
        databaseHelperService.dropDatabase();
    }
}
