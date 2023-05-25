package com.sytoss.producer.bdd.given;

import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import io.cucumber.java.en.Given;

import java.util.List;

public class AnswerGiven extends CucumberIntegrationTest {

    @Given("personal exam with id {word} exists and answers exist")
    public void personalExamWithIdExistsAndAnswersExist(String examId, List<Answer> answers) {

        PersonalExam personalExam = new PersonalExam();
        personalExam.setId(examId);
        personalExam.setAnswers(answers);

        getPersonalExamConnector().save(personalExam);
    }
}
