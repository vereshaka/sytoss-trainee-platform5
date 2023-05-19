package com.sytoss.producer.services;

import com.sytoss.domain.bom.PersonalExam;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalExamService {

    @Autowired
    private PersonalExamConnector personalExamConnector;

    public PersonalExam summary(String id) {
        PersonalExam personalExam = personalExamConnector.getById(id);
        personalExam.summary();
        return personalExam;
    }
}
