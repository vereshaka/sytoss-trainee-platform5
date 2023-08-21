package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.exceptions.business.RequestIsNotValidException;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
                queryResultAnswer = helperServiceProviderObject.getExecuteQueryResult(data.getRequest());
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
                        if (!data.getRequest().contains(condition.getValue())) {
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
        if (queryResultEtalon.getResultMapList().size() != queryResultAnswer.getResultMapList().size()) {
            return false;
        }
        for (int i = 0; i < queryResultEtalon.getResultMapList().size(); i++) {
            List<String> keyListEtalon = queryResultEtalon.getResultMapList().get(i).keySet().stream().toList();
            List<String> keyListAnswer = queryResultAnswer.getResultMapList().get(i).keySet().stream().toList();
            if (keyListAnswer.size() < keyListEtalon.size()) {
                return false;
            }
            boolean allColumnsExists = new HashSet<>(keyListAnswer).containsAll(keyListEtalon);

            if (!allColumnsExists) {
                return false;
            }
            for (String columnName : keyListEtalon) {
                Object etalonFieldValue = queryResultEtalon.getResultMapList().get(i).get(columnName);
                Object answerFieldValue = queryResultAnswer.getResultMapList().get(i).get(columnName);
                if (!Objects.equals(etalonFieldValue, answerFieldValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    public IsCheckEtalon checkEtalon(CheckRequestParameters data) {
        DatabaseHelperService helperServiceProviderObject = databaseHelperServiceProvider.getObject();
        try {
            helperServiceProviderObject.generateDatabase(data.getScript());
            IsCheckEtalon isCheckEtalon = new IsCheckEtalon();

            try {
                helperServiceProviderObject.getExecuteQueryResult(data.getRequest());
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

    public QueryResult checkRequest(CheckRequestParameters data) {
        DatabaseHelperService helperServiceProviderObject = databaseHelperServiceProvider.getObject();
        try {
            helperServiceProviderObject.generateDatabase(data.getScript());
            QueryResult result;

            try {
                result = helperServiceProviderObject.getExecuteQueryResult(data.getRequest());
            } catch (SQLException e) {
                throw new RequestIsNotValidException(e.getMessage());
            }
            return result;
        } finally {
            helperServiceProviderObject.dropDatabase();
        }
    }
}
