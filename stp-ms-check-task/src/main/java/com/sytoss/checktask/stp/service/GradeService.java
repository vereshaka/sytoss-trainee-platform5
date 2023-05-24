package com.sytoss.checktask.stp.service;


import bom.CheckAnswerRequestBody;
import bom.QueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Scope(value = "prototype")
public class GradeService {

    private final DatabaseHelperService databaseHelperService;

    public void checkAndGrade(CheckAnswerRequestBody data) throws Exception {
        databaseHelperService.generateDatabase(data.getScript());
        QueryResult queryResultAnswer = databaseHelperService.getExecuteQueryResult(data.getAnswer());
        QueryResult queryResultEtalon = databaseHelperService.getExecuteQueryResult(data.getEtalon());
        if (queryResultEtalon.getResultMapList().size() == queryResultAnswer.getResultMapList().size()
                && queryResultEtalon.getRow(0) == queryResultAnswer.getRow(0)) {
            databaseHelperService.dropDatabase();
        } else {
            throw new RuntimeException("Data error in database");
        }

    }
}
