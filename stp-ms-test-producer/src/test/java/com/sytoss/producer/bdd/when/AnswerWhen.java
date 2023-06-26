package com.sytoss.producer.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AnswerWhen extends CucumberIntegrationTest {

    @When("student calls answer with value {string} on personal exam with id {word}")
    public void studentCallsAnswer(String answer, String personalExamId) throws JOSEException {
        when(getCheckTaskConnector().checkAnswer(any(CheckTaskParameters.class))).thenReturn(new Score());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("123")));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("studentId", String.valueOf(IntegrationTest.getTestContext().getStudentId()));
        String url = "/api/personalExam/" + personalExamId + "/task/answer";
        HttpEntity<String> request = new HttpEntity<>(answer, headers);
        ResponseEntity<String> responseEntity = doPost(url, request, String.class);
        IntegrationTest.getTestContext().setStatusCode(responseEntity.getStatusCode().value());
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}
