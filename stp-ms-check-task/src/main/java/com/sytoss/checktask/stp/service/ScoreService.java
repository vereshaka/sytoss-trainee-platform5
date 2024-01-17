package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.checktask.exceptions.*;
import com.sytoss.domain.bom.exceptions.business.CheckAnswerIsNotValidException;
import com.sytoss.domain.bom.exceptions.business.RequestIsNotValidException;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        checkRequestParameters.setSortingRelevant(data.isSortingRelevant());

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

        Set<Exception> result = queryResultAnswer.compareWithEtalon(queryResultEtalon, checkRequestParameters);
        List<TaskCondition> failedCondition = new ArrayList<>();

        String studentAnswer = data.getRequest().trim().toUpperCase()
                .replaceAll(">", " > ")
                .replaceAll("<", " < ")
                .replaceAll("=", " = ")
                .replaceAll("!", " ! ")
                .replaceAll(";", " ; ")
                .replaceAll("\\*", " * ")
                .replaceAll("\\.", " . ")
                .replaceAll(",", " , ")
                .replaceAll("\\(", " ( ")
                .replaceAll("\\)", " ) ")
                .replaceAll("\\n", " ")
                .replaceAll("'", "")
                .replaceAll("< {2}=", " <= ")
                .replaceAll("> {2}=", " >= ")
                .replaceAll("( )+", " ");
        List<String> answerWorlds = List.of(studentAnswer.split(" "));

        answerWorlds = answerWorlds.stream().filter(item -> item.trim().length() > 0).toList();

        for (TaskCondition condition : data.getConditions()) {
            List<String> conditionWords = List.of(condition.getValue().trim().toUpperCase().replaceAll("( )+", " ").split(" "));

            conditionWords = conditionWords.stream().filter(item -> item.trim().length() > 0).toList();

            boolean conditionExists = hasCondition(answerWorlds, conditionWords);

            if (condition.getType().equals(ConditionType.CONTAINS) && !conditionExists) {
                failedCondition.add(condition);
                result.add(new CompareConditionException(failedCondition)); // case #1
            } else if (condition.getType().equals(ConditionType.NOT_CONTAINS) && conditionExists) {
                failedCondition.add(condition);
                result.add(new CompareConditionException(failedCondition)); // case #1
            }
        }

        return grade(result);
    }

    private boolean hasCondition(List<String> answerWorlds, List<String> conditionWords) {
        for (String conditionWord : conditionWords) {
            if (!answerWorlds.contains(conditionWord)) {
                return false;
            }
        }
        return true;
    }

    private Score grade(Set<Exception> failedChecks) {
        double grade = 1;
        String comment = "";
        if (failedChecks.size() > 0) {
            for (Exception failedCheck : failedChecks) {
                if (failedCheck instanceof CompareConditionException || failedCheck instanceof WrongSortingException) {
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
        if (data.getCheckAnswer() == null || data.getCheckAnswer().toLowerCase().startsWith("select")) {
            DatabaseHelperService helperServiceProviderObject = databaseHelperServiceProvider.getObject();
            try {
                log.info("Need to create db for " + data.getRequest());
                helperServiceProviderObject.generateDatabase(data.getScript());
                return helperServiceProviderObject.getExecuteQueryResult(data.getRequest(), data.getCheckAnswer());
            } catch (SQLException e) {
                log.error("check query failed", e);
                throw e;
            } finally {
                helperServiceProviderObject.dropDatabase();
            }
        } else {
            throw new CheckAnswerIsNotValidException(data.getCheckAnswer() + " is not valid");
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
