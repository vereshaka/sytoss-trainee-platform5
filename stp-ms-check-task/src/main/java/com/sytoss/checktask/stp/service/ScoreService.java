package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.checktask.exceptions.CompareConditionException;
import com.sytoss.domain.bom.checktask.exceptions.DifferentRowsAmountException;
import com.sytoss.domain.bom.checktask.exceptions.EtalonColumnsNotFoundException;
import com.sytoss.domain.bom.checktask.exceptions.WrongDataException;
import com.sytoss.domain.bom.exceptions.business.CheckAnswerIsNotValidException;
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
            CheckRequestParameters checkRequestParameters = new CheckRequestParameters();
            checkRequestParameters.setScript(data.getScript());
            checkRequestParameters.setRequest(data.getRequest());
            checkRequestParameters.setCheckAnswer(data.getCheckAnswer());

            QueryResult queryResultAnswer;
            try {
                queryResultAnswer = checkQuery(checkRequestParameters);
            } catch (SQLException e) {
                return new Score(0, e.getMessage());
            }

            checkRequestParameters.setRequest(data.getEtalon());

            QueryResult queryResultEtalon;
            try {
                queryResultEtalon = checkQuery(checkRequestParameters);
            } catch (SQLException e) {
                throw new WrongEtalonException("etalon isn't correct", e);
            }

            List<Exception> result = queryResultAnswer.compareWithEtalon(queryResultEtalon);
            List<TaskCondition> failedCondition = new ArrayList<>();
            for (TaskCondition condition : data.getConditions()) {
                if ((condition.getType().equals(ConditionType.CONTAINS)
                        && !data.getRequest().matches("(?i).*\\b"+condition.getValue()+"\\b.*")
                        || (condition.getType().equals(ConditionType.NOT_CONTAINS)
                        && data.getRequest().matches("(?i).*\\b"+condition.getValue()+"\\b.*")
                ))) {
                    failedCondition.add(condition);
                }
            }
            if (failedCondition.size() > 0) {
                result.add(new CompareConditionException(failedCondition)); // case #1
            }
            return grade(result);
    }

    private Score grade(List<Exception> failedChecks) {
        double grade = 1;
        String comment = "";
        if (failedChecks.size() > 0) {
            for (Exception failedCheck : failedChecks) {
                if (failedCheck instanceof CompareConditionException) {
                    grade -= 0.3;
                } else if (failedCheck instanceof DifferentRowsAmountException || failedCheck instanceof WrongDataException
                        || failedCheck instanceof EtalonColumnsNotFoundException) {
                    grade = 0;
                    break;
                }
            }
            comment = String.join("\n", failedChecks.stream().map(Throwable::getMessage).toList());
        }
        return new Score(grade, comment);
    }

    private QueryResult checkQuery(CheckRequestParameters data) throws SQLException {
        if(data.getCheckAnswer()==null || data.getCheckAnswer().toLowerCase().startsWith("select")){
            DatabaseHelperService helperServiceProviderObject = databaseHelperServiceProvider.getObject();
            try {
                helperServiceProviderObject.generateDatabase(data.getScript());
                return helperServiceProviderObject.getExecuteQueryResult(data.getRequest(), data.getCheckAnswer());
            } catch (SQLException e) {
                log.error("check query failed", e);
                throw e;
            } finally {
                helperServiceProviderObject.dropDatabase();
            }
        } else{
            throw new CheckAnswerIsNotValidException(data.getCheckAnswer()+" is not valid");
        }
    }

    public IsCheckEtalon checkEtalon(CheckRequestParameters data) {
        IsCheckEtalon isCheckEtalon = new IsCheckEtalon();
        try {
            checkQuery(data);
            isCheckEtalon.setChecked(true);
        } catch (SQLException e) {
            isCheckEtalon.setChecked(false);
            isCheckEtalon.setException(e.getMessage());
        }
        return isCheckEtalon;
    }

    public QueryResult checkRequest(CheckRequestParameters data) {
        try {
            return checkQuery(data);
        } catch (SQLException e) {
            throw new RequestIsNotValidException(e.getMessage(), e);
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
