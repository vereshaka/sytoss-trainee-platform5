package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;

public class TaskDomainWhen extends CucumberIntegrationTest {

    @When("system try to find task domain by \"(.*)\" id")
    public void systemTryToFindTaskDomainById(String taskDomainId) {
        String url = "/api/taskDomain/" + taskDomainId;

        ResponseEntity<TaskDomain> responseEntity = doGet(url, null, TaskDomain.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
