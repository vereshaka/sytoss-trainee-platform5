package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.Given;

import java.util.List;

public class AnswerGiven extends CucumberIntegrationTest {

    @Given("personal exam with id {word} and student {word} exist")
    public void personalExamWithIdExistsAndAnswersExist(String examId, String studentId, List<Answer> answers) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId(examId);
        personalExam.setStudentId(Long.parseLong(studentId));
        personalExam.setAnswers(answers);
        getPersonalExamConnector().save(personalExam);
        IntegrationTest.getTestContext().setStudentId(personalExam.getStudentId());
    }
}
