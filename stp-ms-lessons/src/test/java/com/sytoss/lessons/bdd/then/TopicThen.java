package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.Then;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TopicThen extends CucumberIntegrationTest {

    @Then("^should return$")
    public void topicShouldBe(List<TopicDTO> topics) {
        List<Topic> topicList = (List<Topic>) TestExecutionContext.getTestContext().getResponse().getBody();
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
}
