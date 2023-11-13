package com.sytoss.producer.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class RatingsWhen extends TestProducerIntegrationTest {
    @When("^teacher gets average estimate for group (.*)$")
    public void studentCallsAnswer(Long groupId) throws JOSEException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", "Teacher"));

        String url = "/api/ratings/average/estimate/" + groupId+"/group";

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<Double> responseEntity = doGet(url, request, Double.class);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
        getTestExecutionContext().getDetails().setRatingsResponse(responseEntity);
    }

    @When("^teacher gets average estimate for the flow (.*)$")
    public void teacherGetsAverageEstimateForTheFlow(Long disciplineId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "", "", "", "Teacher"));

        String url = "/api/ratings/average/estimate/" + disciplineId+"/discipline";

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<Double> responseEntity = doGet(url, request, Double.class);
        getTestExecutionContext().getDetails().setStatusCode(responseEntity.getStatusCode().value());
        getTestExecutionContext().getDetails().setRatingsResponse(responseEntity);
    }
}
