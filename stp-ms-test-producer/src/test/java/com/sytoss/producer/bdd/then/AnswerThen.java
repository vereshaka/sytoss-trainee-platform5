package com.sytoss.producer.bdd.then;

import com.sytoss.domain.bom.personalexam.FirstTask;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnswerThen extends CucumberIntegrationTest {

    @Then("^PersonalExam with \"(.*)\" question should be received$")
    public void questionShouldBe(String question)  {
        FirstTask firstTask = IntegrationTest.getTestContext().getFirstTaskResponse().getBody();
        assertEquals(question, firstTask.getTask().getQuestion());
    }

    @Then("^status of first answer of \"(.*)\" exam for student with (.*) id should be \"(.*)\"$")
    public void answerShouldHaveStatus(String examName, String studentId, String answerStatus) {
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentId(examName, Long.parseLong(studentId));
        assertNotNull(personalExam.getAnswers());
        assertEquals(answerStatus, personalExam.getAnswers().get(0).getStatus().toString());
    }
}
