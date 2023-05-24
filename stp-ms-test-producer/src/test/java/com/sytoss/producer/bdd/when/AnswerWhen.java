package com.sytoss.producer.bdd.when;

import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import com.sytoss.producer.util.CheckAnswerRequestBody;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AnswerWhen extends CucumberIntegrationTest {

    @When("student calls answer with value {string} on personal exam with id {word}")
    public void studentCallsAnswer(String answer, String examId) {

        when(getCheckTaskConnector().checkAnswer(any(CheckAnswerRequestBody.class))).thenReturn(new Grade());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(answer, headers);

        String url = "/api/personalExam/" + examId + "/task/answer";

        ResponseEntity<String> responseEntity = doPost(url, requestEntity, String.class);

        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}

