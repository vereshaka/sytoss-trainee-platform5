package com.sytoss.stp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final DatabaseHelperService databaseHelperService;

    private final QueryResultConvertor queryResultConvertor;

    public void checkAndGrade(String answer, String etalon, String databaseScript) throws Exception {
        databaseHelperService.generateDatabase(databaseScript);
        databaseHelperService.executeQuery("Insert into Answer(answer) values ('" + answer + "')");
        databaseHelperService.executeQuery("Insert into Etalon(etalon) values ('" + etalon + "')");
        databaseHelperService.getExecuteQueryResult();
        databaseHelperService.dropDatabase();
    }
}
