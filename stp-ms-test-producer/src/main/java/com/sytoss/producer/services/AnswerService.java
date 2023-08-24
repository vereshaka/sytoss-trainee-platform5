package com.sytoss.producer.services;

import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import com.sytoss.domain.bom.exceptions.business.RequestIsNotValidException;
import com.sytoss.domain.bom.exceptions.business.StudentDontHaveAccessToPersonalExam;
import com.sytoss.domain.bom.lessons.QueryResult;
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

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
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(personalExam.getStartedDate());
        calendar.add(Calendar.SECOND, personalExam.getTime());
        if (answerUIDate.compareTo(personalExam.getRelevantTo()) >= 0) {
            personalExam.finish();
        }
        personalExamConnector.save(personalExam);
        if (answer == null) {
            return null;
        }

        Question firstTask = new Question();
        ExamModel examModel = new ExamModel();
        examModel.setName(personalExam.getName());
        examModel.setTime(personalExam.getTime());
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
        //TODO:BodenchukY Max grade is always 0, need to set it when create exam
        double gradeValue = score.getValue() * (answer.getTask().getCoef() * (personalExam.getMaxGrade() / personalExam.getSumOfCoef()));
        Grade grade = new Grade();
        grade.setValue(gradeValue);
        grade.setComment(score.getComment());
        answer.grade(grade);
        personalExamConnector.save(personalExam);
    }

    public byte[] getDbImage(String personalExamId) {
        return getImage(personalExamId, ConvertToPumlParameters.DB);
    }

    public byte[] getDataImage(String personalExamId) {
        return getImage(personalExamId, ConvertToPumlParameters.DATA);
    }


    private byte[] getImage(String personalExamId, ConvertToPumlParameters type) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getCurrentAnswer();
        String databaseScript = answer.getTask().getTaskDomain().getDatabaseScript();
        String dataScript = answer.getTask().getTaskDomain().getDataScript();
        return pumlConvertor.generatePngFromPuml(databaseScript + "\n\n" + dataScript, type);
    }

    public QueryResult checkCurrentAnswer(String personalExamId, String taskAnswer) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getCurrentAnswer();
        return check(taskAnswer, answer);
    }

    public QueryResult checkByAnswerId(String personalExamId, String taskAnswer, String answerId) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getAnswerById(Long.valueOf(answerId));
        return check(taskAnswer, answer);
    }

    public QueryResult check(String taskAnswer, Answer answer) {
        CheckRequestParameters request = new CheckRequestParameters();
        request.setRequest(taskAnswer);
        String script = answer.getTask().getTaskDomain().getDatabaseScript() + "\n\n"
                + answer.getTask().getTaskDomain().getDataScript();
        String liquibaseScript = pumlConvertor.convertToLiquibase(script);
        CheckRequestParameters checkRequestParameters = new CheckRequestParameters();
        checkRequestParameters.setRequest(taskAnswer);
        checkRequestParameters.setScript(liquibaseScript);

        request.setScript(liquibaseScript);
        try {
            return checkTaskConnector.testAnswer(request);
        } catch (Exception e) {
            log.error("Error during check request", e);
            if (e instanceof FeignException) {
                throw new RequestIsNotValidException(((FeignException) e).contentUTF8());
            } else throw e;
        }
    }
}