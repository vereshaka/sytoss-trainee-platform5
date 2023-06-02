package com.sytoss.lessons.bdd.when;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;

public class TaskWhen extends CucumberIntegrationTest {

    @When("retrieve information about task by topic")
    public void retrieveInformationAboutTaskByTopic() {
        String url = "/api/topic/" + TestExecutionContext.getTestContext().getTopic().getId() + "/tasks";
        ResponseEntity<String> responseEntity = doGet(url, null, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
