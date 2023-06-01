package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class TaskDomainThen extends CucumberIntegrationTest {

    @Then("^system should been get \"(.*)\" information$")
    public void systemShouldGetTaskDomain(String taskDomainName) {
        TaskDomain result = (TaskDomain) TestExecutionContext.getTestContext().getResponse().getBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(taskDomainName, result.getName());
    }
}
