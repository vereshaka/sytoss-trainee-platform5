package com.sytoss.lessons.bdd.when;

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
    public void requestSentCreatePersonalExam() {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topics";
        ResponseEntity<List<Topic>> responseEntity = doGet(url, null, new ParameterizedTypeReference<List<Topic>>(){});
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher create \"(.*)\" topic$")
    public void topicCreating(String topicName) {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topic";
        Topic topic = new Topic();
        topic.setName(topicName);
        ResponseEntity<Topic> responseEntity = doPost(url, topic, new ParameterizedTypeReference<Topic>(){});
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("teacher creates existing {string} topic")
    public void existingTopicCreating(String topicName) {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topic";
        Topic topic = new Topic();
        topic.setName(topicName);
        ResponseEntity<String> responseEntity = doPost(url, topic, new ParameterizedTypeReference<String>(){});
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
