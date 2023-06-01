package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.Given;

public class TaskDomainGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" task domain exist$")
    public void taskDomainExists(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setName(taskDomainName);
        getTaskDomainConnector().save(taskDomainDTO);
    }
}
