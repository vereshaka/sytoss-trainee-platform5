package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.*;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TaskWhen extends CucumberIntegrationTest {

    @When("retrieve information about this task")
    public void requestSentRetrieveTask() {
        String url = "/api/task/" + TestExecutionContext.getTestContext().getTaskId();
        ResponseEntity<Task> responseEntity = doGet(url, Void.class, Task.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("retrieve information about this existing task")
    public void requestSentRetrieveExistingTask() {
        String url = "/api/task/" + 1;
        ResponseEntity<String> responseEntity = doGet(url, Void.class, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system create task with question \"(.*)\"$")
    public void requestSendCreateTask(String question) {
        String url = "/api/task/";
        Task task = new Task();
        task.setQuestion(question);
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(TestExecutionContext.getTestContext().getTaskDomainId());
        task.setTaskDomain(taskDomain);
        Topic topic = new Topic();
        topic.setId(TestExecutionContext.getTestContext().getTopicId());
        task.setTopics(List.of(topic));
        if (getTaskConnector().getByQuestionAndTopicsDisciplineId(question, TestExecutionContext.getTestContext().getDisciplineId()) == null) {
            ResponseEntity<Task> responseEntity = doPost(url, task, Task.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        } else {
            ResponseEntity<String> responseEntity = doPost(url, task, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }

    @When("^retrieve information about tasks by \"(.*)\" topic in \"(.*)\" discipline$")
    public void retrieveInformationAboutTasksByTopicInDiscipline(String topicName, String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
        String url = "/api/topic/" + topicDTO.getId() + "/tasks";
        ResponseEntity<List<Task>> responseEntity = doGet(url, Void.class, new ParameterizedTypeReference<List<Task>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system add \"(.*)\" condition with (.*) type to task with question \"(.*)\"$")
    public void requestSentAddConditionToTask(String conditionValue, ConditionType type, String taskQuestion) {

        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setType(type);
        taskCondition.setValue(conditionValue);

        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(taskQuestion, TestExecutionContext.getTestContext().getDisciplineId());

        String url = "/api/task/" + taskDTO.getId() + "/condition";

        ResponseEntity<Task> responseEntity = doPost(url, taskCondition, Task.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);

    }
}
