package com.sytoss.producer.services;

import com.sytoss.producer.bom.PersonalExam;
import com.sytoss.producer.bom.Task;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalExamService {

    @Autowired
    private PersonalExamConnector personalExamConnector;

    public Task start(String personalExamId) {
        PersonalExam personalExam = getById(personalExamId);
        personalExam.start();
        personalExam.getAnswers().get(0).inProgress();
        personalExamConnector.save(personalExam);
        return personalExam.getAnswers().get(0).getTask();
    }

    public PersonalExam getById(String personalExamId) {
        return personalExamConnector.getById(personalExamId);
    }
}
