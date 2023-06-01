package com.sytoss.lessons.bdd.then;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.services.TopicService;
import io.cucumber.java.en.Then;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static com.sytoss.lessons.bdd.common.TestExecutionContext.getTestContext;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TopicThen extends CucumberIntegrationTest {

    private TopicService topicService;

    @Then("^should return$")
    public void topicShouldBe(List<TopicDTO> topics) {
        List<Topic> topicList = (List<Topic>) getTestContext().getResponse().getBody();
        int quantityOfTasks = 0;
        assertEquals(topics.size(), topicList.size());
        for (Topic topicFromResponse : topicList) {
            for (TopicDTO topic : topics)
                if (topic.getName().equals(topicFromResponse.getName())) {
                    quantityOfTasks++;
                }
        }
        assertEquals(quantityOfTasks, topicList.size());
    }

    @Then("^should return \"(.*)\" topic")
    public void projectIdShouldExist(String topicName) {
        Topic answer = (Topic) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(topicName, answer.getName());
        assertEquals(TestExecutionContext.getTestContext().getTopicId(), answer.getId());
    }
}
