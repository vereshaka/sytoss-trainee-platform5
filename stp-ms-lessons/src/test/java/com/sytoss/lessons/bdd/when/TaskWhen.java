package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskConditionDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TaskWhen extends CucumberIntegrationTest {

    @When("retrieve information about this task")
    public void requestSentRetrieveTask() throws JOSEException {
        String url = "/api/task/" + TestExecutionContext.getTestContext().getTaskId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Task> responseEntity = doGet(url, httpEntity, Task.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("retrieve information about this existing task")
    public void requestSentRetrieveExistingTask() throws JOSEException {
        String url = "/api/task/" + 1;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = doGet(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system create task with question \"(.*)\"$")
    public void requestSendCreateTask(String question) throws JOSEException {
        String url = "/api/task/";
        Task task = new Task();
        task.setQuestion(question);
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(TestExecutionContext.getTestContext().getTaskDomainId());
        task.setTaskDomain(taskDomain);
        Topic topic = new Topic();
        topic.setId(TestExecutionContext.getTestContext().getTopicId());
        task.setTopics(List.of(topic));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<Task> httpEntity = new HttpEntity<>(task, httpHeaders);
        if (getTaskConnector().getByQuestionAndTopicsDisciplineId(question, TestExecutionContext.getTestContext().getDisciplineId()) == null) {
            ResponseEntity<Task> responseEntity = doPost(url, httpEntity, Task.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        } else {
            ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }

    @When("^retrieve information about tasks by \"(.*)\" topic in \"(.*)\" discipline$")
    public void retrieveInformationAboutTasksByTopicInDiscipline(String topicName, String disciplineName) throws JOSEException {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
        String url = "/api/topic/" + topicDTO.getId() + "/tasks";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Task>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<List<Task>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^remove condition \"(.*)\" and \"(.*)\" type from task$")
    public void removeConditionFromTask(String conditionName, String type) throws JOSEException {
        TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByNameAndType(conditionName, ConditionType.valueOf(type));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        String url = "/api/task/" + TestExecutionContext.getTestContext().getTaskId() + "/condition/" + taskConditionDTO.getId();
        ResponseEntity<Task> responseEntity = doPut(url, httpEntity, Task.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
