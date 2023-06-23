package com.sytoss.checktask.stp.bdd.when;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.domain.bom.personalexam.Grade;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class WhenStepTest extends CheckTaskIntegrationTest {

    @When("request coming to process")
    public void studentsAnswerIsCheckingWith() {
        String url = "/api/task/check";

        HttpHeaders headers = getDefaultHttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CheckTaskParameters> request = new HttpEntity<>(getTestExecutionContext().getDetails().getCheckTaskParameters(), headers);

        ResponseEntity<Grade> responseEntity = doPost(url, request, Grade.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
