package com.sytoss.producer.bdd.when;

import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.When;

import org.springframework.http.ResponseEntity;

public class PersonalExamWhen extends CucumberIntegrationTest {

    @When("the exam with id {word} is done")
    public void theExamIsDoneOnTask(String examId) {
        String url = "/api/personalExam/" + examId + "/summary";

        ResponseEntity<String> responseEntity = getRestTemplate().postForEntity(url, Void.class, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}