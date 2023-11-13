package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.Given;

import java.util.List;

public class RatingsGiven extends TestProducerIntegrationTest {

    @Given("personal exams are exist")
    public void personalExamsAreExist(List<PersonalExam> personalExamList) {
        for (PersonalExam personalExam : personalExamList) {
            if (getPersonalExamConnector().getByNameAndStudentUid(personalExam.getName(), personalExam.getStudent().getUid()) == null) {
                getPersonalExamConnector().save(personalExam);
            }
        }
    }
}
