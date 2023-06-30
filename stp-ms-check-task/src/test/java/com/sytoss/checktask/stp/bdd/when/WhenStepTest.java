package com.sytoss.checktask.stp.bdd.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.checktask.stp.bdd.CucumberIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class WhenStepTest extends CucumberIntegrationTest {

    @When("request coming to process")
    public void studentsAnswerIsCheckingWith() throws JsonProcessingException {
        String url = "/api/task/check";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestLine = new ObjectMapper().writeValueAsString(TestContext.getInstance().getCheckTaskParameters());
        HttpEntity<String> request = new HttpEntity<>(requestLine, headers);

        ResponseEntity<String> responseEntity = doPost(url, request, String.class);
        TestContext.getInstance().setResponseEntity(responseEntity);
    }

    @When("^request sent to check etalon answer$")
    public void requestSentCheckEtalonAnswer() throws JsonProcessingException {
        String url = "/api/task/check-etalon";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestLine = new ObjectMapper().writeValueAsString(TestContext.getInstance().getCheckTaskParameters());
        HttpEntity<String> request = new HttpEntity<>(requestLine, headers);

        ResponseEntity<String> responseEntity = doPost(url, request, String.class);
        TestContext.getInstance().setResponseEntity(responseEntity);
    }

    @When("^request sent to check \"request\"$")
    public void requestSentCheckRequest() throws JsonProcessingException {
        String url = "/api/task/check-request";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestLine = new ObjectMapper().writeValueAsString(TestContext.getInstance().getCheckTaskParameters());
        HttpEntity<String> request = new HttpEntity<>(requestLine, headers);

        ResponseEntity<String> responseEntity = doPost(url, request, String.class);
        TestContext.getInstance().setResponseEntity(responseEntity);
    }
}
