package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.checktask.exceptions.CompareConditionException;
import com.sytoss.domain.bom.checktask.exceptions.DifferentRowsAmountException;
import com.sytoss.domain.bom.checktask.exceptions.EtalonColumnsNotFoundException;
import com.sytoss.domain.bom.checktask.exceptions.WrongDataException;
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
                queryResultAnswer = data.isQueryForUpdate() ? helperServiceProviderObject.getExecuteQueryUpdate(data.getRequest()) :
                        helperServiceProviderObject.getExecuteQueryResult(data.getRequest());
            } catch (SQLException e) {
                return new Score(0, e.getMessage());
            }
            helperServiceProviderObject = databaseHelperServiceProvider.getObject();
            helperServiceProviderObject.generateDatabase(data.getScript());
            QueryResult queryResultEtalon;
            try {
                queryResultEtalon = data.isQueryForUpdate() ? helperServiceProviderObject.getExecuteQueryUpdate(data.getEtalon()) :
                        helperServiceProviderObject.getExecuteQueryResult(data.getEtalon());
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
            if (failedCondition.size() > 0) {
                result.add(new CompareConditionException(failedCondition)); // case #1
            }
            return grade(result);
        } finally {
            helperServiceProviderObject.dropDatabase();
        }
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

    public IsCheckEtalon checkEtalon(CheckRequestParameters data) {
        DatabaseHelperService helperServiceProviderObject = databaseHelperServiceProvider.getObject();
        try {
            helperServiceProviderObject.generateDatabase(data.getScript());
            IsCheckEtalon isCheckEtalon = new IsCheckEtalon();

            try {
                if(data.isQueryForUpdate()){
                    helperServiceProviderObject.getExecuteQueryUpdate(data.getRequest());
                }else{
                    helperServiceProviderObject.getExecuteQueryResult(data.getRequest());
                }
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
                result = data.isQueryForUpdate() ? helperServiceProviderObject.getExecuteQueryUpdate(data.getRequest()) :
                        helperServiceProviderObject.getExecuteQueryResult(data.getRequest());
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
