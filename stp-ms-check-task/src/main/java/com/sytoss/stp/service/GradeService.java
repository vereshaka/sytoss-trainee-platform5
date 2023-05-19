package com.sytoss.stp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final DatabaseHelperService databaseHelperService;

    private final QueryResultConvertor queryResultConvertor;

    public void checkAndGrade(String answer, String etalon, String databaseScript) throws Exception {
        Connection connection = databaseHelperService.generateDatabase(databaseScript);
        databaseHelperService.executeQuery("Insert into Answer(answer) values ("+answer+")");
        databaseHelperService.executeQuery("Insert into Etalon(etalon) values ("+etalon+")");
        queryResultConvertor.getExecuteQueryResult(connection);
        databaseHelperService.dropDatabase();
    }
}
