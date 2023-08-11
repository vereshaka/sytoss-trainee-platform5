package com.sytoss.producer.services;

import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import com.sytoss.domain.bom.exceptions.business.StudentDontHaveAccessToPersonalExam;
import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.MetadataConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnswerService extends AbstractService {

    private final MetadataConnector metadataConnector;

    private final PersonalExamConnector personalExamConnector;

    private final CheckTaskConnector checkTaskConnector;

    private final PumlConvertor pumlConvertor;

    public Question answer(String personalExamId, String taskAnswer) {
        Long studentId = getCurrentUser().getId();
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        if (!Objects.equals(personalExam.getStudent().getId(), studentId)) {
            throw new StudentDontHaveAccessToPersonalExam(studentId, personalExamId);
        }
        Answer answer = personalExam.getCurrentAnswer();
        answer.answer(taskAnswer);
        checkAnswer(answer, personalExam);
        answer = personalExam.getNextAnswer();
        personalExamConnector.save(personalExam);

        Question firstTask = new Question();
        ExamModel examModel = new ExamModel();
        examModel.setName(personalExam.getName());
        examModel.setTime(personalExam.getTime());
        examModel.setAmountOfTasks(personalExam.getAmountOfTasks());
        firstTask.setExam(examModel);
        TaskModel taskModel = new TaskModel();
        taskModel.setQuestion(answer.getTask().getQuestion());
        taskModel.setSchema(answer.getTask().getTaskDomain().getDatabaseScript());
        taskModel.setQuestionNumber(
                personalExam.getAnswers().size()
                        - personalExam.getAnswers().stream()
                        .filter(item -> item.getStatus().equals(AnswerStatus.NOT_STARTED))
                        .collect(Collectors.toUnmodifiableList()).size() + 1);

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
        String script = taskDomain.getDatabaseScript() + StringUtils.LF + taskDomain.getDataScript();
        String liquibase = pumlConvertor.convertToLiquibase(script);
        checkTaskParameters.setScript(liquibase);
        Score score = checkTaskConnector.checkAnswer(checkTaskParameters);
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

    public QueryResult check(String personalExamId, String taskAnswer) {
        CheckRequestParameters request = new CheckRequestParameters();
        request.setRequest(taskAnswer);
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        Answer answer = personalExam.getCurrentAnswer();
        String databaseScript = answer.getTask().getTaskDomain().getDatabaseScript();
        String dataScript = answer.getTask().getTaskDomain().getDataScript();
        request.setScript(databaseScript + "\n\n" + dataScript);
        return checkTaskConnector.testAnswer(request);
    }
}