package com.sytoss.producer.bdd.when;

import com.sytoss.checktaskshared.util.CheckTaskParameters;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.When;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AnswerWhen extends CucumberIntegrationTest {

    @When("student calls answer with value {string} on personal exam with id {word}")
    public void studentCallsAnswer(String answer, String personalExamId) {
        when(getCheckTaskConnector().checkAnswer(any(CheckTaskParameters.class))).thenReturn(new Grade());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("studentId", String.valueOf(IntegrationTest.getTestContext().getStudentId()));
        String url = "/api/personalExam/" + personalExamId + "/task/answer";
        HttpEntity<String> request = new HttpEntity<>(answer, headers);
        ResponseEntity<String> responseEntity = doPost(url, request, String.class);
        IntegrationTest.getTestContext().setStatusCode(responseEntity.getStatusCode().value());
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}
