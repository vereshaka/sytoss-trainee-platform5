package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.Given;

public class TaskDomainGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" task domain exists$")
    public void taskDomainExist(String taskDomainName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(TestExecutionContext.getTestContext().getDisciplineId());
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, disciplineDTO.getId());
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.setName(taskDomainName);
            taskDomainDTO.setScript("Test script");
            taskDomainDTO.setDiscipline(disciplineDTO);
            getTaskDomainConnector().save(taskDomainDTO);
        }
        TestExecutionContext.getTestContext().setTaskDomainId(taskDomainDTO.getId());
    }

    @Given("^\"(.*)\" task domain doesnt exist$")
    public void taskDomainNotExist(String name) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(name, TestExecutionContext.getTestContext().getDisciplineId());
        if (taskDomainDTO != null) {
            getTaskDomainConnector().delete(taskDomainDTO);
        }
    }

    @Given("^\"(.*)\" task domain with \"(.*)\" script exists for \"(.*)\" discipline$")
    public void taskDomainForDiscipline(String nameTaskDomain, String script, String nameDiscipline) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(nameDiscipline);
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(nameTaskDomain, disciplineDTO.getId());
        if(taskDomainDTO == null){
            taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.setName(nameTaskDomain);
            taskDomainDTO.setDiscipline(disciplineDTO);
            taskDomainDTO.setScript(script);
            taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);
        }
        TestExecutionContext.getTestContext().setTaskDomainId(taskDomainDTO.getId());
    }

    @Given("^\"(.*)\" task domain with \"(.*)\" script does not exist$")
    public void taskDomainNotExist(String nameTaskDomain, String script) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndScript(nameTaskDomain, script);
        if(taskDomainDTO != null){
           getTaskDomainConnector().delete(taskDomainDTO);
        }
    }
}
