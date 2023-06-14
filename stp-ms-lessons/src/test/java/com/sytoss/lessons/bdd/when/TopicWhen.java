package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TopicWhen extends CucumberIntegrationTest {

    private final String URI = "/api/";

    @When("^system retrieve information about topics by discipline$")
    public void requestSentCreatePersonalExam() {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topics";
        ResponseEntity<List<Topic>> responseEntity = doGet(url, null, new ParameterizedTypeReference<List<Topic>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^retrieve information about topic by topicID$")
    public void getById() {
        Long topicId = TestExecutionContext.getTestContext().getTopicId();
        if (TestExecutionContext.getTestContext().getTopicId() == null) {
            topicId = 99L;
            String url = "/api/topic/" + topicId;
            ResponseEntity<String> responseEntity = doGet(url, null, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        } else {
            String url = "/api/topic/" + topicId;
            ResponseEntity<Topic> responseEntity = doGet(url, null, new ParameterizedTypeReference<Topic>() {
            });
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }

    @When("^teacher create \"(.*)\" topic$")
    public void topicCreating(String topicName) {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topic";
        Topic topic = new Topic();
        topic.setName(topicName);
        ResponseEntity<Topic> responseEntity = doPost(url, topic, Topic.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("teacher creates existing {string} topic")
    public void existingTopicCreating(String topicName) {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/topic";
        Topic topic = new Topic();
        topic.setName(topicName);
        ResponseEntity<String> responseEntity = doPost(url, topic, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^assign topic \"(.*)\" to this task$")
    public void linkTopicToThisTask(String topicName) {
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, TestExecutionContext.getTestContext().getDisciplineId());
        String url = "/api/task/" + TestExecutionContext.getTestContext().getTaskId() + "/topic/" + topicDTO.getId();
        ResponseEntity<Task> responseEntity = doPost(url, null, Task.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
