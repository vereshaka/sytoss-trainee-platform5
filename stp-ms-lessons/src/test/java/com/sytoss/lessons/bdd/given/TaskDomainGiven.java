package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.Given;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

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
            taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);
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

    @Given("^task domains exist$")
    public void taskdomainExist(List<TaskDomainDTO> taskDomains) {
        for (TaskDomainDTO taskDomainDTO : taskDomains) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(taskDomainDTO.getDiscipline().getName(), TestExecutionContext.getTestContext().getTeacherId());
            if (disciplineDTO == null) {
                disciplineDTO = taskDomainDTO.getDiscipline();
                disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }
            TaskDomainDTO result = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainDTO.getName(), disciplineDTO.getId());
            taskDomainDTO.setDiscipline(disciplineDTO);
            if (result == null) {
                getTaskDomainConnector().save(taskDomainDTO);
            }
        }
    }

    @Given("^\"(.*)\" task domain with \"(.*)\" script exists for this discipline$")
    public void taskDomainForDiscipline(String taskDomainName, String script) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, TestExecutionContext.getTestContext().getDisciplineId());
        if(taskDomainDTO == null){
            taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.setName(taskDomainName);
            taskDomainDTO.setDiscipline(getDisciplineConnector().getReferenceById(TestExecutionContext.getTestContext().getDisciplineId()));
            taskDomainDTO.setScript(script);
            taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);
        }
        TestExecutionContext.getTestContext().setTaskDomainId(taskDomainDTO.getId());
    }

    @Given("^\"(.*)\" task domain doesn't have image$")
    public void taskDomainShouldNotHaveImage(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, TestExecutionContext.getTestContext().getDisciplineId());
        if (taskDomainDTO.getImage() != null) {
            taskDomainDTO.setImage(null);
            getTaskDomainConnector().save(taskDomainDTO);
        }
    }
}