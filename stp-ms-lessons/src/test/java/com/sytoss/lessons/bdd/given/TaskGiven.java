package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.*;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;

public class TaskGiven extends CucumberIntegrationTest {

    @Given("task with question {string} exist")
    public void taskExist(String question) {

        TaskDTO taskDTO = getTaskConnector().getByQuestion(question);
        if (taskDTO == null) {
            taskDTO = new TaskDTO();
            taskDTO.setQuestion(question);
            taskDTO.setEtalonAnswer("Etalon answer");
            TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.setId(7L);
            taskDTO.setTaskDomain(taskDomainDTO);
            List<TopicDTO> topics = new ArrayList<>();
            taskDTO.setTopics(topics);

            getTaskConnector().saveAndFlush(taskDTO);
        }
        TestExecutionContext.getTestContext().setTaskId(taskDTO.getId());
    }

    @Given("task with question {string} and id {long} doesnt exist")
    public void taskNotExist(String question, long taskId) {
        TaskDTO taskDTO = getTaskConnector().getByQuestion(question);
        if (taskDTO != null) {
            getTaskConnector().delete(taskDTO);
        }
        TestExecutionContext.getTestContext().setTaskId(3L);
    }
}
