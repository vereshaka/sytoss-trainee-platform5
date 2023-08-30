package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bom.TaskDomainRequestParameters;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskConditionDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TaskWhen extends LessonsIntegrationTest {

    @When("retrieve information about this task")
    public void requestSentRetrieveTask() {
        String url = "/api/task/" + getTestExecutionContext().getDetails().getTaskId();
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Task> responseEntity = doGet(url, httpEntity, Task.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("retrieve information about this existing task")
    public void requestSentRetrieveExistingTask() {
        String url = "/api/task/" + 1;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = doGet(url, httpEntity, String.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^retrieve information about task with id (.*)$")
    public void requestSentRetrieveExistingTask(String taskId) {
        String url = "/api/task/" + getTestExecutionContext().getIdMapping().get(taskId);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = doGet(url, httpEntity, String.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^system create task with question \"(.*)\"$")
    public void requestSendCreateTask(String question) {
        String url = "/api/task";
        Task task = new Task();
        task.setQuestion(question);
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(getTestExecutionContext().getDetails().getTaskDomainId());
        task.setTaskDomain(taskDomain);
        Topic topic = new Topic();
        topic.setId(getTestExecutionContext().getDetails().getTopicId());
        Discipline discipline = new Discipline();
        discipline.setId(getTestExecutionContext().getDetails().getDisciplineId());
        Teacher teacher = new Teacher();
        teacher.setId(getTestExecutionContext().getDetails().getTeacherId());
        discipline.setTeacher(teacher);
        topic.setDiscipline(discipline);
        task.setTopics(List.of(topic));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Task> httpEntity = new HttpEntity<>(task, httpHeaders);
        if (getTaskConnector().getByQuestionAndTopicsDisciplineIdAndDeleteDateIsNull(question, getTestExecutionContext().getDetails().getDisciplineId()) == null) {
            ResponseEntity<Task> responseEntity = doPost(url, httpEntity, Task.class);
            getTestExecutionContext().setResponse(responseEntity);
        } else {
            ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
            getTestExecutionContext().setResponse(responseEntity);
        }
    }

    @When("^retrieve information about tasks of topic with id (.*)$")
    public void retrieveInformationAboutTasksByTopicInDiscipline(String topicKey) {
        String url = "/api/topic/" + getTestExecutionContext().getIdMapping().get(topicKey) + "/tasks";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Task>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^retrieve information about tasks by \"(.*)\" topic in \"(.*)\" discipline$")
    public void retrieveInformationAboutTasksByTopicInDiscipline(String topicName, String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
        String url = "/api/topic/" + topicDTO.getId() + "/tasks";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Task>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^remove condition \"(.*)\" and \"(.*)\" type from task$")
    public void removeConditionFromTask(String conditionName, String type) {
        TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByValueAndType(conditionName, ConditionType.valueOf(type));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(getToken());
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        String url = "/api/task/" + getTestExecutionContext().getDetails().getTaskId() + "/condition/" + taskConditionDTO.getId();
        ResponseEntity<Task> responseEntity = doPut(url, httpEntity, Task.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^system add \"(.*)\" condition with (.*) type to task with question \"(.*)\"$")
    public void requestSentAddConditionToTask(String conditionValue, ConditionType type, String taskQuestion) {
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setId(getTestExecutionContext().getDetails().getTaskConditionId());
        taskCondition.setType(type);
        taskCondition.setValue(conditionValue);
        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineIdAndDeleteDateIsNull(taskQuestion, getTestExecutionContext().getDetails().getDisciplineId());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskCondition> httpEntity = new HttpEntity<>(taskCondition, httpHeaders);
        String url = "/api/task/" + taskDTO.getId() + "/condition";
        ResponseEntity<Task> responseEntity = doPut(url, httpEntity, Task.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^request is \"(.*)\" sent to check this request for this task domain$")
    public void requestSentCheckRequest(String request) {
        String url = "/api/task/check-request-result";

        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ID", 1);
        hashMap.put("NAME", "SQL");
        list.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("ID", 2);
        hashMap.put("NAME", "Mongo");
        list.add(hashMap);
        QueryResult queryResult = new QueryResult();
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", 1);
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        when(getCheckTaskConnector().checkRequest(any())).thenReturn(queryResult);

        TaskDomainRequestParameters taskDomainRequestParameters = new TaskDomainRequestParameters();
        taskDomainRequestParameters.setTaskDomainId(getTestExecutionContext().getDetails().getTaskDomainId());
        taskDomainRequestParameters.setRequest(request);
        HttpEntity<TaskDomainRequestParameters> requestEntity = new HttpEntity<>(taskDomainRequestParameters, httpHeaders);
        ResponseEntity<QueryResult> responseEntity = doPost(url, requestEntity, QueryResult.class);

        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("delete task")
    public void requestSentDeleteTask() {
        String url = "/api/task/" + getTestExecutionContext().getDetails().getTaskId() + "/delete";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Task> responseEntity = doPost(url, httpEntity, Task.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}