package com.sytoss.checktask.stp.bdd.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class WhenStepTest extends CheckTaskIntegrationTest {

    @When("request coming to process")
    public void studentsAnswerIsCheckingWith() throws JsonProcessingException {
        String url = "/api/task/check";

        HttpHeaders headers = getDefaultHttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestLine = new ObjectMapper().writeValueAsString(getTestExecutionContext().getDetails().getCheckTaskParameters());
        HttpEntity<String> request = new HttpEntity<>(requestLine, headers);

        ResponseEntity<Object> responseEntity1 = doPost(url, request, Object.class);
        ResponseEntity<Score> responseEntity = doPost(url, request, Score.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^request sent to check etalon answer$")
    public void requestSentCheckEtalonAnswer() throws JsonProcessingException {
        String url = "/api/task/check-etalon";

        HttpHeaders headers =getDefaultHttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestLine = new ObjectMapper().writeValueAsString(getTestExecutionContext().getDetails().getCheckTaskParameters());
        HttpEntity<String> request = new HttpEntity<>(requestLine, headers);

        ResponseEntity<IsCheckEtalon> responseEntity = doPost(url, request, IsCheckEtalon.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
