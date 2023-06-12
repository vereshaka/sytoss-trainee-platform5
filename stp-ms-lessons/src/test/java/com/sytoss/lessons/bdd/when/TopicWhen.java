package com.sytoss.lessons.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
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
    public void requestSentCreatePersonalExam() throws JOSEException {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topics";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("find_topics_by_discipline")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Topic>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<List<Topic>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^retrieve information about topic by topicID$")
    public void getById() throws JOSEException {
        Long topicId = TestExecutionContext.getTestContext().getTopicId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("get_topic")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        if (TestExecutionContext.getTestContext().getTopicId() == null) {
            topicId = 99L;
            String url = "/api/topic/" + topicId;
            ResponseEntity<String> responseEntity = doGet(url, httpEntity, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        } else {
            String url = "/api/topic/" + topicId;
            ResponseEntity<Topic> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<Topic>() {
            });
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }

    @When("^teacher create \"(.*)\" topic$")
    public void topicCreating(String topicName) throws JOSEException {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topic";
        Topic topic = new Topic();
        topic.setName(topicName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("create_topic")));
        HttpEntity<Topic> httpEntity = new HttpEntity<>(topic, httpHeaders);
        ResponseEntity<Topic> responseEntity = doPost(url, httpEntity, Topic.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("teacher creates existing {string} topic")
    public void existingTopicCreating(String topicName) throws JOSEException {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topic";
        Topic topic = new Topic();
        topic.setName(topicName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("create_topic")));
        HttpEntity<Topic> httpEntity = new HttpEntity<>(topic, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
