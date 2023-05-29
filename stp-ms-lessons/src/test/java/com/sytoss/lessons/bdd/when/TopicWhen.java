package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TopicWhen extends CucumberIntegrationTest {

    private final String URI = "/api/";

    @When("^system retrieve information about topics by discipline$")
    public void requestSentCreatePersonalExam() {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topics";
        ResponseEntity<List<Topic>> responseEntity = doGet(url, null, new ParameterizedTypeReference<List<Topic>>(){});
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher create \"(.*)\" topic$")
    public void topicCreating(String topicName) {
        String url = getBaseUrl() + URI + "discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topic";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Topic topic = new Topic();
        topic.setName(topicName);
        HttpEntity<Topic> request = new HttpEntity<>(topic, httpHeaders);
        ResponseEntity<Topic> responseEntity = getRestTemplate().postForEntity(url, request, Topic.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("teacher create {string} topic that already created")
    public void existingTopicCreating(String topicName) {
        String url = getBaseUrl() + URI + "discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topic";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Topic topic = new Topic();
        topic.setName(topicName);
        HttpEntity<Topic> request = new HttpEntity<>(topic, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().postForEntity(url, request, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
