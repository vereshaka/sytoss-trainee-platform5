package com.sytoss.lessons.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TopicWhen extends LessonsIntegrationTest {

    private final String URI = "/api/";

    @When("^system retrieve information about topics by discipline$")
    public void requestSentCreatePersonalExam() throws JOSEException {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/topics";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Topic>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^retrieve information about topic by topicID$")
    public void getById() throws JOSEException {
        Long topicId = getTestExecutionContext().getDetails().getTopicId();
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        if (getTestExecutionContext().getDetails().getTopicId() == null) {
            topicId = 99L;
            String url = "/api/topic/" + topicId;
            ResponseEntity<String> responseEntity = doGet(url, httpEntity, String.class);
            getTestExecutionContext().setResponse(responseEntity);
        } else {
            String url = "/api/topic/" + topicId;
            ResponseEntity<Topic> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
            });
            getTestExecutionContext().setResponse(responseEntity);
        }
    }

    @When("^teacher create \"(.*)\" topic$")
    public void topicCreating(String topicName) {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/topic";
        Topic topic = new Topic();
        topic.setName(topicName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Topic> httpEntity = new HttpEntity<>(topic, httpHeaders);
        ResponseEntity<Topic> responseEntity = doPost(url, httpEntity, Topic.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher creates existing \"(.*)\" topic$")
    public void existingTopicCreating(String topicName) {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/topic";
        Topic topic = new Topic();
        topic.setName(topicName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Topic> httpEntity = new HttpEntity<>(topic, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^assign topic \"(.*)\" to this task$")
    public void linkTopicToThisTask(String topicName) {
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, getTestExecutionContext().getDetails().getDisciplineId());
        String url = "/api/topic/" + topicDTO.getId() + "task/" + getTestExecutionContext().getDetails().getTaskId();
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Task> responseEntity = doPost(url, httpEntity, Task.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^receive this topic's icon$")
    public void getTopicIcon() {
        String url = "/api/topic/" + getTestExecutionContext().getDetails().getTopicId() + "/icon";
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> responseEntity = doGet(url, requestEntity, byte[].class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
