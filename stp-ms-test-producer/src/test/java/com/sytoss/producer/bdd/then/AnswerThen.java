package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.Question;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnswerThen extends TestProducerIntegrationTest {

    @Then("^PersonalExam with \"(.*)\" question should be received$")
    public void questionShouldBe(String question) {
        Question firstTask = getTestExecutionContext().getDetails().getFirstTaskResponse().getBody();
    }

    @Then("^status of first answer of \"(.*)\" exam for this student should be \"(.*)\"$")
    public void answerShouldHaveStatus(String examName,  String answerStatus) {
        answerShouldHaveStatus(examName, getTestExecutionContext().getDetails().getStudentUid(), answerStatus);
    }

    @Then("^status of first answer of \"(.*)\" exam for student with (.*) id should be \"(.*)\"$")
    public void answerShouldHaveStatus(String examName, String studentId, String answerStatus) {
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentUid(examName, studentId);
        assertNotNull(personalExam.getAnswers());
        assertEquals(answerStatus, personalExam.getAnswers().get(0).getStatus().toString());
    }

    @Then("^answer with id (.*) of personal exam with id (.*) should have value \"(.*)\" and change status to (.*) and grade (.*)$")
    public void taskWithNumberShouldHaveAnswer(String answerId, String examId, String string, String status, String grade) {

        PersonalExam personalExam = getPersonalExamConnector().getById(getTestExecutionContext().replaceId(examId).toString());

        Optional<Answer> foundAnswer = personalExam.getAnswers().stream()
                .filter(answerTmp -> answerTmp.getId().equals(Long.parseLong(answerId)))
                .findFirst();

        Answer answer = foundAnswer.orElse(null);

        Assertions.assertEquals(answer.getValue(), string);
        assertEquals(Double.valueOf(grade), answer.getGrade().getValue());
        Assertions.assertEquals(answer.getStatus(), AnswerStatus.valueOf(status));
    }

    @Then("^response should return next task with number (.*)$")
    public void responseShouldReturnNextTask(Integer taskNumber) throws JsonProcessingException {

        Question nextAnswer = getMapper().readValue(getTestExecutionContext().getDetails().getResponse().getBody(), Question.class);

        Assertions.assertEquals(taskNumber, nextAnswer.getTask().getQuestionNumber());

    }
}
