package com.sytoss.producer.services;

import com.sytoss.common.AbstractStpService;
import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.exceptions.business.StudentDontHaveAccessToPersonalExam;
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

@RequiredArgsConstructor
@Service
public class AnswerService extends AbstractService {

    private final MetadataConnector metadataConnector;

    private final PersonalExamConnector personalExamConnector;

    private final CheckTaskConnector checkTaskConnector;

    private final PumlConvertor pumlConvertor;

    public Answer answer(String personalExamId, String taskAnswer) {
        Long studentId = getCurrentUser().getId();
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        if (!Objects.equals(personalExam.getStudent().getUid(), studentId)) {
            throw new StudentDontHaveAccessToPersonalExam(personalExamId, studentId);
        }
        Answer answer = personalExam.getCurrentAnswer();
        answer.answer(taskAnswer);
        checkAnswer(answer, personalExam);
        personalExamConnector.save(personalExam);
        return personalExam.getNextAnswer();
    }

    @Async
    void checkAnswer(Answer answer, PersonalExam personalExam) {
        Task task = metadataConnector.getTaskById(answer.getTask().getId());
        TaskDomain taskDomain = metadataConnector.getTaskDomain(answer.getTask().getTaskDomain().getId());
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
}