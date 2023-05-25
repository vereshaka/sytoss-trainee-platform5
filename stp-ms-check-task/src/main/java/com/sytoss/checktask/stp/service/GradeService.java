package com.sytoss.checktask.stp.service;


import bom.CheckAnswerRequestBody;
import bom.QueryResult;
import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final DatabaseHelperService databaseHelperService;

    public void checkAndGrade(CheckAnswerRequestBody data) {
        databaseHelperService.generateDatabase(data.getScript());
        QueryResult queryResultAnswer = databaseHelperService.getExecuteQueryResult(data.getAnswer());
        QueryResult queryResultEtalon = databaseHelperService.getExecuteQueryResult(data.getEtalon());
        if (queryResultEtalon.getResultMapList().size() == queryResultAnswer.getResultMapList().size()
                && queryResultEtalon.getRow(0).size() == queryResultAnswer.getRow(0).size()) {
            databaseHelperService.dropDatabase();
        } else {
            throw new DatabaseCommunicationError("Data error in database");
        }
    }
}
