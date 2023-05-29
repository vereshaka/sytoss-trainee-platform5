package com.sytoss.producer.services;

import com.sytoss.checktaskshared.util.CheckTaskParameters;
import com.sytoss.domain.bom.exceptions.businessException.StudentDontHaveAccessToPersonalExam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.MetadataConnectorImpl;
import com.sytoss.producer.connectors.PersonalExamConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final MetadataConnectorImpl metadataConnector = new MetadataConnectorImpl();

    private final PersonalExamConnector personalExamConnector;

    private final CheckTaskConnector checkTaskConnector;

    public Answer answer(String personalExamId, Long studentId, String taskAnswer) {
        PersonalExam personalExam = personalExamConnector.getById(personalExamId);
        if (!Objects.equals(personalExam.getStudentId(), studentId)) {
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
        checkTaskParameters.setAnswer(answer.getValue());
        checkTaskParameters.setEtalon(task.getEtalonAnswer());
        checkTaskParameters.setScript(taskDomain.getScript());
        Grade grade = checkTaskConnector.checkAnswer(checkTaskParameters);
        answer.grade(grade);
        personalExamConnector.save(personalExam);
    }

}
