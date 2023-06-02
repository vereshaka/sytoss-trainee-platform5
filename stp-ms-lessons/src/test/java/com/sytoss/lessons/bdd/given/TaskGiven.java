package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.*;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Optional;

public class TaskGiven extends CucumberIntegrationTest {

    @Given("^task with question \"(.*)\" exists$")
    public void taskExists(String question) {

        Optional<TaskDomainDTO> optionalTaskDomainDTO = getTaskDomainConnector().findById(TestExecutionContext.getTestContext().getTaskDomainId());
        TaskDomainDTO taskDomainDTO = optionalTaskDomainDTO.orElse(null);

        assert taskDomainDTO != null;

        Optional<TopicDTO> optionalTopicDTO = getTopicConnector().findById(TestExecutionContext.getTestContext().getTopicId());
        TopicDTO topicDTO = optionalTopicDTO.orElse(null);

        assert topicDTO != null;

        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(question, TestExecutionContext.getTestContext().getDisciplineId());

        if (taskDTO == null) {
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