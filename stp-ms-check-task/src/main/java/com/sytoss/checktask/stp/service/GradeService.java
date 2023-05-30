package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.checktask.model.QueryResult;
import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import com.sytoss.domain.bom.personalexam.Grade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeService {

    private final ObjectProvider<DatabaseHelperService> databaseHelperServiceProvider;

    public Grade checkAndGrade(CheckTaskParameters data) {
        DatabaseHelperService helperServiceProviderObject = databaseHelperServiceProvider.getObject();
        try {
            helperServiceProviderObject.generateDatabase(data.getScript());
            QueryResult queryResultAnswer;
            try {
                queryResultAnswer = helperServiceProviderObject.getExecuteQueryResult(data.getAnswer());
            } catch (SQLException e) {
                return new Grade(0, e.getMessage());
            }

            QueryResult queryResultEtalon;
            try {
                queryResultEtalon = helperServiceProviderObject.getExecuteQueryResult(data.getEtalon());
            } catch (SQLException e) {
                throw new WrongEtalonException("etalon isn't correct", e);
            }
            return grade(queryResultEtalon, queryResultAnswer);
        } finally {
            helperServiceProviderObject.dropDatabase();
        }
    }

    private Grade grade(QueryResult queryResultEtalon, QueryResult queryResultAnswer) {
        if (queryResultEtalon.getResultMapList().size() != queryResultAnswer.getResultMapList().size() && queryResultEtalon.getRow(0).size() != queryResultAnswer.getRow(0).size()) {
            return new Grade(1, "not ok");
        }
        return new Grade(10, "ok");
    }
}
