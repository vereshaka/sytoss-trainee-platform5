package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;

public class TaskDomainWhen extends CucumberIntegrationTest {

    @When("^system retrieve information about \"(.*)\" task domain$")
    public void systemTryToFindTaskDomainById(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByName(taskDomainName);
        String url = "/api/taskDomain/" + taskDomainDTO.getId();
        ResponseEntity<TaskDomain> responseEntity = doGet(url, null, TaskDomain.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
