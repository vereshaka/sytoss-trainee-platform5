package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.Given;

public class TaskDomainGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" task domain with \"(.*)\" id exist$")
    public void taskDomainExists(String taskDomainName, Long taskDomainId) {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(taskDomainId);
        taskDomainDTO.setName(taskDomainName);

        getTaskDomainConnector().save(taskDomainDTO);
    }
}
