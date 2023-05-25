package com.sytoss.lessons.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.Then;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TopicThen extends CucumberIntegrationTest {

    @Then("^should return$")
    public void topicShouldBe(List<TopicDTO> topics) throws JsonProcessingException {
        List<Topic> topicList = getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), new TypeReference<>() {
        });
        int quantityOfTasks = 0;
        assertEquals(topics.size(), topicList.size());
        for (Topic topic : topicList) {
            for (TopicDTO topicFromResponse : topics)
                if (topic.getName().equals(topicFromResponse.getName())) {
                    quantityOfTasks++;
                }
        }
        assertEquals(quantityOfTasks, topicList.size());
    }
}
