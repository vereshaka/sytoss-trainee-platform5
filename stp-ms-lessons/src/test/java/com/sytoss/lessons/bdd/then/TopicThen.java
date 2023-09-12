package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.services.TopicService;
import io.cucumber.java.en.Then;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopicThen extends LessonsIntegrationTest {

    private TopicService topicService;

    @Then("^should return$")
    public void topicShouldBe(List<Topic> topics) {
        List<Topic> topicList = (List<Topic>) getTestExecutionContext().getResponse().getBody();
        int quantityOfTasks = 0;
        assertEquals(topics.size(), topicList.size());
        for (Topic topicFromResponse : topicList) {
            for (Topic topic : topics)
                if (topic.getName().equals(topicFromResponse.getName())) {
                    quantityOfTasks++;
                }
        }
        assertEquals(quantityOfTasks, topicList.size());
    }

    @Then("^should return \"(.*)\" topic")
    public void topicShouldExist(String topicName) {
        Topic answer = (Topic) getTestExecutionContext().getResponse().getBody();
        assertEquals(topicName, answer.getName());
        assertEquals(getTestExecutionContext().getDetails().getTopicId(), answer.getId());
    }

    @Then("^topic's icon should be received$")
    public void topicIconShouldBeReceived() {
        TopicDTO topicDTO = getTopicConnector().findById(getTestExecutionContext().getDetails().getTopicId()).orElse(null);
        byte[] icon = (byte[]) getTestExecutionContext().getResponse().getBody();
        assertEquals(topicDTO.getIcon().length, icon.length);
    }
}
