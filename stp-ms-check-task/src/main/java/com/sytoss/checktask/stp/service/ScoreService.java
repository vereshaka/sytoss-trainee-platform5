package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.checktask.exceptions.CompareConditionException;
import com.sytoss.domain.bom.exceptions.business.RequestIsNotValidException;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
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
            List<Exception> result = queryResultAnswer.compareWithEtalon(queryResultEtalon);
            List<TaskCondition> failedCondition = new ArrayList<>();
            for (TaskCondition condition : data.getConditions()) {
                if ((condition.getType().equals(ConditionType.CONTAINS)
                        && !StringUtils.containsIgnoreCase(data.getRequest(), condition.getValue()))
                        || (condition.getType().equals(ConditionType.NOT_CONTAINS)
                        && StringUtils.containsIgnoreCase(data.getRequest(), condition.getValue()))
                ) {
                    failedCondition.add(condition);
                }
            }
            if (failedCondition.size() > 0){
                result.add(new CompareConditionException(failedCondition)); // case #1
            }
            return grade(result);
        } finally {
            helperServiceProviderObject.dropDatabase();
        }
    }

    private Score grade(List<Exception> failedChecks) {
        double grade = 1;
        String comment = "OK";
        if (failedChecks.size() > 0){
            //TODO: yevgenyv: make a grade and fill comment
        }
        return new Score(grade, comment);
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

    public boolean checkValidation(String script) {
        DatabaseHelperService helperServiceProviderObject = databaseHelperServiceProvider.getObject();
        try {
            helperServiceProviderObject.generateDatabase(script);
            helperServiceProviderObject.dropDatabase();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
