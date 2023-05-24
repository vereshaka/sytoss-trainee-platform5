package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertNotNull(personalExam.getAnswers());
        assertEquals(answerStatus, personalExam.getAnswers().get(0).getStatus().toString());
    }

    @Then("answer with id {int} of personal exam with id {word} should have value {string} and change status to {word}")
    public void taskWithNumberShouldHaveAnswer(int answerId, String examId, String string, String status) {

        PersonalExam personalExam = getPersonalExamConnector().getById(examId);

        Answer answer = new Answer();

        Optional<Answer> foundAnswer = personalExam.getAnswers().stream().filter(answerTmp -> answerTmp.getId() == answerId).findFirst();

        if (foundAnswer.isPresent()) {
            answer = foundAnswer.get();
        }

        Assertions.assertEquals(answer.getValue(), string);
        Assertions.assertEquals(answer.getStatus(), AnswerStatus.valueOf(status));
    }

    @Then("response should return next task")
    public void responseShouldReturnNextTask(Answer answer) throws JsonProcessingException {

        Answer nextAnswer = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), Answer.class);

        Assertions.assertEquals(answer.getId(), nextAnswer.getId());
        Assertions.assertEquals(answer.getTask().getQuestion(), nextAnswer.getTask().getQuestion());
        Assertions.assertEquals(answer.getStatus(), nextAnswer.getStatus());
    }
}
