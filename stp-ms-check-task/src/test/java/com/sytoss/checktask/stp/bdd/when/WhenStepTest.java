package com.sytoss.checktask.stp.bdd.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class WhenStepTest extends CheckTaskIntegrationTest {

    @When("request coming to process")
    public void studentsAnswerIsCheckingWith() {
        String url = "/api/task/check";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CheckTaskParameters> request = new HttpEntity<>(getTestExecutionContext().getDetails().getCheckTaskParameters(), headers);

        ResponseEntity<Score> responseEntity = doPost(url, request, Score.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^request sent to check etalon answer$")
    public void requestSentCheckEtalonAnswer() throws JsonProcessingException {
        String url = "/api/task/check-etalon";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestLine = new ObjectMapper().writeValueAsString(getTestExecutionContext().getDetails().getCheckTaskParameters());
        HttpEntity<String> request = new HttpEntity<>(requestLine, headers);

        ResponseEntity<IsCheckEtalon> responseEntity = doPost(url, request, IsCheckEtalon.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^request sent to check$")
    public void requestSentCheckRequest() throws JsonProcessingException {
        String url = "/api/task/check-request";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestLine = new ObjectMapper().writeValueAsString(getTestExecutionContext().getDetails().getCheckTaskParameters());
        HttpEntity<String> request = new HttpEntity<>(requestLine, headers);

        ResponseEntity<QueryResult> responseEntity = doPost(url, request, QueryResult.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^request sent to check incorrect script$")
    public void requestSentToCheckIncorrectScript() throws JsonProcessingException {
        String url = "/api/task/check-request";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestLine = new ObjectMapper().writeValueAsString(getTestExecutionContext().getDetails().getCheckTaskParameters());
        HttpEntity<String> request = new HttpEntity<>(requestLine, headers);

        ResponseEntity<String> responseEntity = doPost(url, request, String.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
