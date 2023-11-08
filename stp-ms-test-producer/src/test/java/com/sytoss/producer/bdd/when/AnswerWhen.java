package com.sytoss.producer.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.personalexam.AnswerModule;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AnswerWhen extends TestProducerIntegrationTest {

    @When("^student calls answer with value \"(.*)\" on personal exam with id (.*)$")
    public void studentCallsAnswer(String answer, String personalExamId) throws JOSEException {

        Score score = new Score();
        score.setValue(1);
        when(getCheckTaskConnector().checkAnswer(any(CheckTaskParameters.class))).thenReturn(score);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), String.valueOf(getTestExecutionContext().getDetails().getStudentId())));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        String url = "/api/personal-exam/" + getTestExecutionContext().replaceId(personalExamId) + "/task/answer";

        AnswerModule body = new AnswerModule();
        body.setAnswer(answer);
        body.setAnswerUIDate(new Date());
        body.setTimeSpent(1000L);
        HttpEntity<AnswerModule> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, request, String.class);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
        getTestExecutionContext().getDetails().setResponse(responseEntity);
    }
}
