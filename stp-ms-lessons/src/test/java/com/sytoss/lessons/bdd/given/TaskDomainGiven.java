package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.Given;

public class TaskDomainGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" task domain exists$")
    public void taskDomainExists(String taskDomainName) {

        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByName(taskDomainName);
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.setName(taskDomainName);
            taskDomainDTO.setScript("Test script");
            taskDomainDTO = getTaskDomainConnector().saveAndFlush(taskDomainDTO);
        }
        TestExecutionContext.getTestContext().setTaskDomainId(taskDomainDTO.getId());
    }

    @Given("^\"(.*)\" task domain doesnt exist$")
    public void taskDomainNotExist(String name) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByName(name);
        if (taskDomainDTO != null) {
            getTaskDomainConnector().delete(taskDomainDTO);
        }
    }
}
