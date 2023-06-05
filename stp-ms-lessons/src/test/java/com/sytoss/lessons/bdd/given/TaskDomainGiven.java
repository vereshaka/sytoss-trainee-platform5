package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.Given;

public class TaskDomainGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" task domain doesnt exist$")
    public void taskDomainNotExist(String name) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByName(name);
        if (taskDomainDTO != null) {
            getTaskDomainConnector().delete(taskDomainDTO);
        }
    }

    @Given("^\"(.*)\" task domain exist$")
    public void taskDomainExist(String name) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByName(name);
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.
            taskDomainDTO.setName(name);
            getTaskDomainConnector().save(taskDomainDTO);
        }
    }
}
