package com.sytoss.checktask.stp.service;


import bom.CheckAnswerRequestBody;
import com.sytoss.domain.bom.QueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final DatabaseHelperService databaseHelperService;

    public void checkAndGrade(CheckAnswerRequestBody data) throws Exception {
        databaseHelperService.generateDatabase(data.getScript());
        QueryResult queryResult = databaseHelperService.getExecuteQueryResult(data.getAnswer(), data.getEtalon());
        databaseHelperService.dropDatabase();
    }
}
