package com.sytoss.producer.services;

import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import com.sytoss.domain.bom.exceptions.business.RequestIsNotValidException;
import com.sytoss.domain.bom.exceptions.business.StudentDontHaveAccessToPersonalExam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.MetadataConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class AnswerService extends AbstractService {

    private final MetadataConnector metadataConnector;

    private final PersonalExamConnector personalExamConnector;

    private final CheckTaskConnector checkTaskConnector;

    private final PumlConvertor pumlConvertor;

    public Question answer(String personalExamId, String taskAnswer, Date answerUIDate, Long timeSpent) {
        Long studentId = getCurrentUser().getId();
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        if (!Objects.equals(personalExam.getStudent().getId(), studentId)) {
            throw new StudentDontHaveAccessToPersonalExam(studentId, personalExamId);
        }
        Answer answer = personalExam.getCurrentAnswer();
        if (answer == null) {
            return null;
        }
        answer.setAnswerDate(new Date());
        answer.setAnswerUIDate(answerUIDate);
        answer.setTimeSpent(timeSpent);
        answer.answer(taskAnswer);
        checkAnswer(answer, personalExam);
        answer = personalExam.getNextAnswer();
        personalExamConnector.save(personalExam);
        if (answer == null) {
            return null;
        }
        Question firstTask = new Question();
        ExamModel examModel = new ExamModel();
        examModel.setName(personalExam.getName());
        examModel.setTime((int) TimeUnit.MILLISECONDS.toSeconds(personalExam.getRelevantTo().getTime() - new Date().getTime()));
        examModel.setAmountOfTasks(personalExam.getAmountOfTasks());
        firstTask.setExam(examModel);
        TaskModel taskModel = new TaskModel();
        Long processedQuestionsNum = personalExam.getAnswers().stream()
                .filter(item -> item.getStatus().equals(AnswerStatus.ANSWERED)
                        || item.getStatus().equals(AnswerStatus.GRADED))
                .count();
        taskModel.setQuestionNumber((int) (processedQuestionsNum + 1L));

        firstTask.setTask(taskModel);

        return firstTask;
    }

    @Async
    void checkAnswer(Answer answer, PersonalExam personalExam) {
        Task task = answer.getTask();
        TaskDomain taskDomain = task.getTaskDomain();
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest(answer.getValue());
        checkTaskParameters.setEtalon(task.getEtalonAnswer());
        checkTaskParameters.setConditions(task.getTaskConditions());
        String script = taskDomain.getDatabaseScript() + StringUtils.LF + StringUtils.LF + taskDomain.getDataScript();
        String liquibase = pumlConvertor.convertToLiquibase(script);
        checkTaskParameters.setScript(liquibase);
        Score score = checkTaskConnector.checkAnswer(checkTaskParameters);
        double gradeValue = score.getValue() * (answer.getTask().getCoef() * (personalExam.getMaxGrade() / personalExam.getSumOfCoef()));
        Grade grade = new Grade();
        grade.setValue(gradeValue);
        grade.setComment(score.getComment());
        answer.grade(grade);
        answer.setScore(score);
        personalExamConnector.save(personalExam);
    }

    public String getDbImage(String personalExamId) {
        return getImage(personalExamId, ConvertToPumlParameters.DB);
    }

    public String getDataImage(String personalExamId) {
        return getImage(personalExamId, ConvertToPumlParameters.DATA);
    }


    private String getImage(String personalExamId, ConvertToPumlParameters type) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getCurrentAnswer();
        if (type == ConvertToPumlParameters.DB) {
            return answer.getTask().getTaskDomain().getDbImageName();
        } else {
            return answer.getTask().getTaskDomain().getDataImageName();
        }
    }

    public QueryResult checkCurrentAnswer(String personalExamId, String taskAnswer) {
        String parsedTaskAnswer = taskAnswer.replaceAll("\\n", " ");
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getCurrentAnswer();

        return check(parsedTaskAnswer, answer);
    }

    public QueryResult checkByAnswerId(String personalExamId, String taskAnswer, String answerId) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getAnswerById(Long.valueOf(answerId));
        return check(taskAnswer, answer);
    }

    public QueryResult check(String taskAnswer, Answer answer) {
        CheckRequestParameters request = new CheckRequestParameters();
        String script = answer.getTask().getTaskDomain().getDatabaseScript() + "\n\n"
                + answer.getTask().getTaskDomain().getDataScript();
        String liquibaseScript = pumlConvertor.convertToLiquibase(script);
        request.setRequest(taskAnswer);
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