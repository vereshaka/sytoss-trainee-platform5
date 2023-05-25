package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TopicWhen extends CucumberIntegrationTest {

    private final String URI = "/api/";

    @When("^system retrieve information about topics by discipline$")
    public void requestSentCreatePersonalExam() {
        String url = getBaseUrl() + URI + IntegrationTest.getTestContext().getDisciplineId() + "/topics";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().getForEntity(url, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}
