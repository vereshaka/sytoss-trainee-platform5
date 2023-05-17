package com.sytoss.traineeplatform.services;

import com.sytoss.traineeplatform.bom.Answer;
import com.sytoss.traineeplatform.bom.PersonalExam;
import com.sytoss.traineeplatform.bom.Task;
import com.sytoss.traineeplatform.repositories.PersonalExamConnector;
import com.sytoss.traineeplatform.requestbodies.AnswerRequestBody;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerService {

    private final PersonalExamConnector personalExamConnector;

    public AnswerService(PersonalExamConnector personalExamConnector) {
        this.personalExamConnector = personalExamConnector;
    }

    public Task answer(long idTest, int taskNumber, AnswerRequestBody answerRequestBody) {
        Optional<PersonalExam> optionalPersonalExam = personalExamConnector.findById(idTest);
        PersonalExam personalExam = optionalPersonalExam.get();

        Answer answer = personalExam.getAnswers().get(taskNumber);

        answer.setValue(answerRequestBody.getValue());
        answer.setStatus(answerRequestBody.getStatus());

        answer.changeStatus();

        Thread thread = new Thread(() -> personalExamConnector.checkAnswer(answer));

        thread.start();

        Task nextTask = personalExam.getAnswers().get(taskNumber + 1).getTask();

        return nextTask;
    }
}
