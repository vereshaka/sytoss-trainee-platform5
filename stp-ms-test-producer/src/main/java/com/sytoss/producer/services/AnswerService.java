package com.sytoss.producer.services;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.MetadataConnectorImpl;
import com.sytoss.producer.connectors.PersonalExamConnector;
import com.sytoss.producer.util.CheckTaskParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    private final MetadataConnectorImpl metadataConnector = new MetadataConnectorImpl();

    @Autowired
    private PersonalExamConnector personalExamConnector;

    @Autowired
    private CheckTaskConnector checkTaskConnector;

    public Answer answer(String examId, String taskAnswer) {
        PersonalExam personalExam = personalExamConnector.getById(examId);

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

        answer.changeStatus(grade);

        personalExamConnector.save(personalExam);
    }

}
