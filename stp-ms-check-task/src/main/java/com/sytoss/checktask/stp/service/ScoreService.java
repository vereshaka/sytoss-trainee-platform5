package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.checktask.model.QueryResult;
import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.CheckEtalonParametrs;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScoreService {

    private final ObjectProvider<DatabaseHelperService> databaseHelperServiceProvider;

    public Score checkAndScore(CheckTaskParameters data) {
        DatabaseHelperService helperServiceProviderObject = databaseHelperServiceProvider.getObject();
        try {
            helperServiceProviderObject.generateDatabase(data.getScript());
            QueryResult queryResultAnswer;
            try {
                queryResultAnswer = helperServiceProviderObject.getExecuteQueryResult(data.getAnswer());
            } catch (SQLException e) {
                return new Score(0, e.getMessage());
            }

            QueryResult queryResultEtalon;
            try {
                queryResultEtalon = helperServiceProviderObject.getExecuteQueryResult(data.getEtalon());
            } catch (SQLException e) {
                throw new WrongEtalonException("etalon isn't correct", e);
            }
            Score score = grade(queryResultEtalon, queryResultAnswer);
            if (score.getValue() > 0) {
                for (TaskCondition condition : data.getConditions()) {
                    if (condition.getType().equals(ConditionType.CONTAINS)) {
                        if (!data.getAnswer().contains(condition.getValue())) {
                            score.setValue(score.getValue() - 0.3);
                            break;
                        }
                    }
                }
            }
            return score;
        } finally {
            helperServiceProviderObject.dropDatabase();
        }
    }

    private Score grade(QueryResult queryResultEtalon, QueryResult queryResultAnswer) {
        if (!checkQueryResults(queryResultEtalon, queryResultAnswer)) {
            return new Score(0, "not ok");
        }
        return new Score(1, "ok");
    }

    private boolean checkQueryResults(QueryResult queryResultEtalon, QueryResult queryResultAnswer) {
        if (queryResultEtalon.getResultMapList().size() != queryResultAnswer.getResultMapList().size() && queryResultEtalon.getRow(0).size() != queryResultAnswer.getRow(0).size()) {
            return false;
        }
        for (int i = 0; i < queryResultEtalon.getResultMapList().size(); i++) {
            List<String> keyListEtalon = queryResultEtalon.getResultMapList().get(i).keySet().stream().toList();
            List<String> keyListAnswer = queryResultAnswer.getResultMapList().get(i).keySet().stream().toList();
            if (keyListAnswer.size() != keyListEtalon.size()) {
                return false;
            }
            boolean allColumnsExists = keyListEtalon.stream()
                    .allMatch(keyListAnswer::contains);

            if (!allColumnsExists) {
                return false;
            }
            for (String columnName : keyListEtalon) {
                Object etalonFieldValue = queryResultEtalon.getResultMapList().get(i).get(columnName);
                Object answerFieldValue = queryResultAnswer.getResultMapList().get(i).get(columnName);
                if (!etalonFieldValue.equals(answerFieldValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    public IsCheckEtalon checkEtalon(CheckEtalonParametrs data) {
        DatabaseHelperService helperServiceProviderObject = databaseHelperServiceProvider.getObject();
        try {
            helperServiceProviderObject.generateDatabase(data.getScript());
            IsCheckEtalon isCheckEtalon = new IsCheckEtalon();

            try {
                helperServiceProviderObject.getExecuteQueryResult(data.getEtalon());
            } catch (SQLException e) {
                isCheckEtalon.setChecked(false);
                isCheckEtalon.setException(e.getMessage());
                return isCheckEtalon;
            }
            isCheckEtalon.setChecked(true);
            return isCheckEtalon;
        } finally {
            helperServiceProviderObject.dropDatabase();
        }
    }
}
