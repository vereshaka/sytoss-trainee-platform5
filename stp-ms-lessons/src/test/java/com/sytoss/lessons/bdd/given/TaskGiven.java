package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;

public class TaskGiven extends CucumberIntegrationTest {

    @Given("this topic has task with question {string}")
    public void thisTopicHasTaskWithQuestion(String question) {

        Task task = new Task();
        task.setId(1L);
        task.setQuestion(question);
        task.setTaskDomain(taskDomain);
        task.setTopics(List.of(getTopicConnector().getReferenceById(TestExecutionContext.getTestContext().getTopicId())));

        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        getTaskDomainConvertor().toDTO(taskDomain, taskDomainDTO);
        getTaskDomainConnector().save(taskDomainDTO);

        TaskDTO taskDTO = new TaskDTO();
        getTaskConvertor().toDTO(task, taskDTO);
        getTaskConnector().save(taskDTO);
    }

    @And("task with question {string} doesnt have this topic")
    public void taskWithQuestionDoesntHaveThisTopic(String question) {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(1L);
        taskDomain.setName("Task Domain");
        taskDomain.setScript("Script");

        Task task = new Task();
        task.setId(1L);
        task.setQuestion(question);
        task.setTaskDomain(taskDomain);
        task.setTopics(new ArrayList<>());

        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        getTaskDomainConvertor().toDTO(taskDomain, taskDomainDTO);
        getTaskDomainConnector().save(taskDomainDTO);

        TaskDTO taskDTO = new TaskDTO();
        getTaskConvertor().toDTO(task, taskDTO);
        getTaskConnector().save(taskDTO);
    }
}
