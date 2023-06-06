package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
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

    @When("retrieve information about tasks by topic")
    public void retrieveInformationAboutTaskByTopic() {
        String url = "/api/topic/" + TestExecutionContext.getTestContext().getTopicId() + "/tasks";
        ResponseEntity<List<Task>> responseEntity = doGet(url, Void.class, new ParameterizedTypeReference<List<Task>>() {});
       // ResponseEntity<String> responseEntity = doGet(url, Void.class, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
