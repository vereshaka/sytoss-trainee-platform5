package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
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
       /* String url = getBaseUrl() + URI + IntegrationTest.getTestContext().getDisciplineId() + "/topics";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().getForEntity(url, String.class);
        IntegrationTest.getTestContext().setResponse(responseEntity);*/

        String url = "/api/discipline/" + IntegrationTest.getTestContext().getDisciplineId() + "/topics";
        ResponseEntity<List<Topic>> responseEntity = doGet(url, null, new ParameterizedTypeReference<List<Topic>>(){});
        IntegrationTest.getTestContext().setStatus(responseEntity.getStatusCode().value());
        IntegrationTest.getTestContext().setListOfTopicResponse(responseEntity);
    }

    @When("^teacher create \"(.*)\" topic$")
    public void topicCreating(String topicName) {
        String url = getBaseUrl() + URI + "discipline/" + IntegrationTest.getTestContext().getDisciplineId() + "/topic";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Topic topic = new Topic();
        topic.setName(topicName);
        HttpEntity<Topic> request = new HttpEntity<>(topic, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().postForEntity(url, request, String.class);
        // проверить работу кода
        IntegrationTest.getTestContext().setStatus(responseEntity.getStatusCode().value());
        IntegrationTest.getTestContext().setResponse(responseEntity);
    }
}
