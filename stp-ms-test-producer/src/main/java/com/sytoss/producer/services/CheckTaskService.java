package com.sytoss.producer.services;

import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.exceptions.business.RequestIsNotValidException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
@Slf4j
public class CheckTaskService {

    private final PersonalExamConnector personalExamConnector;

    private final CheckTaskConnector checkTaskConnector;

    private final PumlConvertor pumlConvertor;

    @Async
    public void checkAnswer(Answer answer, PersonalExam personalExam) {
        Task task = answer.getTask();
        TaskDomain taskDomain = task.getTaskDomain();
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest(answer.getValue());
        if (task.getCheckAnswer() != null) {
            checkTaskParameters.setCheckAnswer(task.getCheckAnswer());
        }
        checkTaskParameters.setEtalon(task.getEtalonAnswer());
        checkTaskParameters.setConditions(task.getTaskConditions());
        checkTaskParameters.setSortingRelevant(task.isSortingRelevant());
        String script = taskDomain.getDatabaseScript() + StringUtils.LF + StringUtils.LF + taskDomain.getDataScript();
        String liquibase = pumlConvertor.convertToLiquibase(script);
        checkTaskParameters.setScript(liquibase);
        Score score = checkTaskConnector.checkAnswer(checkTaskParameters);
        double gradeValue = score.getValue() * (answer.getTask().getCoef() * personalExam.getMaxGrade() / personalExam.getSumOfCoef());
        gradeValue = new BigDecimal(gradeValue).setScale(1, RoundingMode.HALF_UP).doubleValue();
        Grade grade = new Grade();
        grade.setValue(gradeValue);
        grade.setComment(score.getComment());
        answer.grade(grade);
        answer.setScore(score);
        personalExamConnector.save(personalExam);
    }

    public QueryResult checkCurrentAnswer(String personalExamId, String taskAnswer, String checkAnswer) {
        String parsedTaskAnswer = taskAnswer.replaceAll("\\n", " ");
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getCurrentAnswer();

        return check(parsedTaskAnswer, answer,checkAnswer);
    }

    public QueryResult checkByAnswerId(String personalExamId, String taskAnswer, String answerId, String checkAnswer) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getAnswerById(Long.valueOf(answerId));
        return check(taskAnswer, answer,checkAnswer);
    }

    private QueryResult check(String taskAnswer, Answer answer, String checkAnswer) {
        CheckRequestParameters request = new CheckRequestParameters();
        String script = answer.getTask().getTaskDomain().getDatabaseScript() + "\n\n"
                + answer.getTask().getTaskDomain().getDataScript();
        String liquibaseScript = pumlConvertor.convertToLiquibase(script);
        request.setRequest(taskAnswer);
        request.setCheckAnswer(checkAnswer);
        request.setScript(liquibaseScript);
        try {
            QueryResult queryResult = checkTaskConnector.testAnswer(request);
            return queryResult;
        } catch (Exception e) {
            if (e instanceof FeignException) {
                throw new RequestIsNotValidException(((FeignException) e).contentUTF8());
            }
            throw e;
        }
    }
}
