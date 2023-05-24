package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.CheckAnswerRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final DatabaseHelperService databaseHelperService;

    public void checkAndGrade(CheckAnswerRequestBody data) throws Exception {
        databaseHelperService.generateDatabase(data.getScript());
        databaseHelperService.getExecuteQueryResult(data.getAnswer(), data.getEtalon());
        databaseHelperService.dropDatabase();
    }
}
