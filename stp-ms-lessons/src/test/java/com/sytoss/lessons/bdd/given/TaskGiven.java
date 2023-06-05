package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.Given;

import java.util.List;

public class TaskGiven extends CucumberIntegrationTest {

    @Given("^task with question \"(.*)\" exists$")
    public void taskExists(String question) {

        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(question, TestExecutionContext.getTestContext().getDisciplineId());

        if (taskDTO == null) {
            TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getReferenceById(TestExecutionContext.getTestContext().getTaskDomainId());
            TopicDTO topicDTO = getTopicConnector().getReferenceById(TestExecutionContext.getTestContext().getTopicId());
            taskDTO = new TaskDTO();
            taskDTO.setQuestion(question);
            taskDTO.setEtalonAnswer("Etalon answer");
            taskDTO.setTaskDomain(taskDomainDTO);
            taskDTO.setTopics(List.of(topicDTO));
            getTaskConnector().save(taskDTO);
        }
        TestExecutionContext.getTestContext().setTaskId(taskDTO.getId());
    }

    @Given("^task with question \"(.*)\" doesnt exist$")
    public void taskNotExist(String question) {
        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(question, TestExecutionContext.getTestContext().getDisciplineId());
        if (taskDTO != null) {
            getTaskConnector().delete(taskDTO);
        }
    }
}