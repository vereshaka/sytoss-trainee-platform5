package com.sytoss.provider.bdd.when;

import com.sytoss.provider.bdd.CucumberIntegrationTest;
import com.sytoss.provider.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class ImageWhen extends CucumberIntegrationTest {

    @When("^retrieve image by id (.*)")
    public void retrieveImageById(Long id) {
        String url = "/api/question-image/" + id;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<byte[]> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<byte[]> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
