package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskDomainThen extends CucumberIntegrationTest {

    @Then("^\"(.*)\" task domain should be created$")
    public void groupsShouldBeReceived(String name) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(name, TestExecutionContext.getTestContext().getDisciplineId());
        TaskDomain taskDomain = (TaskDomain) TestExecutionContext.getTestContext().getResponse().getBody();
        assertNotNull(taskDomainDTO);
        assertEquals(taskDomain.getId(), taskDomainDTO.getId());
        assertEquals(taskDomain.getName(), taskDomainDTO.getName());
    }

    @Then("^system should been get \"(.*)\" information$")
    public void systemShouldGetTaskDomain(String taskDomainName) {
        TaskDomain taskDomain = (TaskDomain) TestExecutionContext.getTestContext().getResponse().getBody();
        Assertions.assertNotNull(taskDomain);
        Assertions.assertEquals(taskDomainName, taskDomain.getName());
    }

    @Then("^\"(.*)\" task domain with \"(.*)\" script should be$")
    public void taskDomainWithScriptShouldBe(String nameTaskDomain, String script) {
        TaskDomain taskDomain = (TaskDomain) TestExecutionContext.getTestContext().getResponse().getBody();
        assertNotNull(taskDomain);
        assertEquals(nameTaskDomain, taskDomain.getName());
        assertEquals(script, taskDomain.getScript());
    }
}
