package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnswerThen extends CucumberIntegrationTest {

    @Then("^should return \"(.*)\" question$")
    public void questionShouldBe(String question) throws JsonProcessingException {
        Task task = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), new TypeReference<>() {
        });
        assertEquals(question, task.getQuestion());
    }

    @Then("^status of first answer of \"(.*)\" exam for student with (.*) id should be \"(.*)\"$")
    public void answerShouldHaveStatus(String examName, String studentId, String answerStatus) {
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentId(examName, Long.parseLong(studentId));
        assertEquals(answerStatus, personalExam.getAnswers().get(0).getStatus().toString());
    }
}
