package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.Given;

public class TaskDomainGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" task domain exist$")
    public void taskDomainExists(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByName(taskDomainName);
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.setName(taskDomainName);
            getTaskDomainConnector().save(taskDomainDTO);
        }
    }

    @Given("^\"(.*)\" task domain does not exist$")
    public void taskDomainDoesNotExist(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByName(taskDomainName);
        if (taskDomainDTO != null) {
            getTaskDomainConnector().deleteById(taskDomainDTO.getId());
        }
    }
}
