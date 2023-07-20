package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.stp.test.FileUtils;
import io.cucumber.java.en.Given;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class TaskDomainGiven extends LessonsIntegrationTest {

    @Given("^\"(.*)\" task domain exists$")
    public void taskDomainExist(String taskDomainName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, disciplineDTO.getId());
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.setName(taskDomainName);
            taskDomainDTO.setDatabaseScript("Test script");
            String databaseScript = FileUtils.readFromFile("puml/database.puml");
            taskDomainDTO.setDatabaseScript(databaseScript);
            String dataScript = FileUtils.readFromFile("puml/data.puml");
            taskDomainDTO.setDataScript(dataScript);
            taskDomainDTO.setDiscipline(disciplineDTO);
            taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);
        }
        getTestExecutionContext().getDetails().setTaskDomainId(taskDomainDTO.getId());
    }

    @Given("^\"(.*)\" task domain doesnt exist$")
    public void taskDomainNotExist(String name) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(name, getTestExecutionContext().getDetails().getDisciplineId());
        if (taskDomainDTO != null) {
            getTaskDomainConnector().delete(taskDomainDTO);
        }
    }

    @Given("^task domains exist$")
    public void taskDomainExist(List<TaskDomainDTO> taskDomains) {
        for (TaskDomainDTO taskDomainDTO : taskDomains) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(taskDomainDTO.getDiscipline().getName(), getTestExecutionContext().getDetails().getTeacherId());
            if (disciplineDTO == null) {
                disciplineDTO = taskDomainDTO.getDiscipline();
                disciplineDTO.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }
            TaskDomainDTO result = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainDTO.getName(), disciplineDTO.getId());
            taskDomainDTO.setDiscipline(disciplineDTO);
            if (result == null) {
                String stringId="";
                if (taskDomainDTO.getId() != null) {
                    stringId = "*" + taskDomainDTO.getId();
                    taskDomainDTO.setId(null);
                }
                TaskDomainDTO taskDomain = getTaskDomainConnector().save(taskDomainDTO);
                if (!stringId.equals("")){
                    getTestExecutionContext().getIdMapping().put(stringId,taskDomain.getId());
                }
            }
        }
        getTestExecutionContext().getDetails().setTaskDomains(taskDomains);
    }

    @Given("^\"(.*)\" task domain with a script from \"(.*)\" exists for this discipline$")
    public void taskDomainForDiscipline(String taskDomainName, String path) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, getTestExecutionContext().getDetails().getDisciplineId());
        String scriptFromFile = FileUtils.readFromFile("liquibase/" + path);
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.setName(taskDomainName);
            taskDomainDTO.setDiscipline(getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId()));
            taskDomainDTO.setDatabaseScript(scriptFromFile);
            taskDomainDTO.setDataScript(scriptFromFile);
            taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);
        }
        getTestExecutionContext().getDetails().setTaskDomainId(taskDomainDTO.getId());
    }

    @Given("^\"(.*)\" task domain doesn't have image$")
    public void taskDomainShouldNotHaveImage(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, getTestExecutionContext().getDetails().getDisciplineId());
        if (taskDomainDTO.getImage() != null) {
            taskDomainDTO.setImage(null);
            getTaskDomainConnector().save(taskDomainDTO);
        }
    }
}