package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskDomainThen extends CucumberIntegrationTest {

    @Then("^\"(.*)\" task domain should be created$")
    public void groupsShouldBeReceived(String name) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByName(name);
        TaskDomain taskDomain = (TaskDomain) TestExecutionContext.getTestContext().getResponse().getBody();
        assertNotNull(taskDomainDTO);
        assertEquals(taskDomain.getId(), taskDomainDTO.getId());
        assertEquals(taskDomain.getName(), taskDomainDTO.getName());
    }
}
