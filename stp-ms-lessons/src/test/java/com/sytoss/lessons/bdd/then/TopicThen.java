package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.services.TopicService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Then("tasks for this topic")
    public void tasksForThisTopic(DataTable dataTable) {
        List<Map<String, String>> taskRows = dataTable.asMaps();
        List<Task> tasksFromFeature = new ArrayList<>();
        for (Map<String, String> taskRow : taskRows) {
            Task task = new Task();
            String taskIdKey = (String) getTestExecutionContext().replaceId(taskRow.get("taskId"));
            task.setId(Long.parseLong(taskIdKey));
            task.setQuestion(taskRow.get("question"));
            tasksFromFeature.add(task);
        }

        List<TaskDTO> taskDTOList = getTaskConnector().findByTopicsIdOrderByCode(getTestExecutionContext().getDetails().getTopicId());
        assertEquals(tasksFromFeature.size(), taskDTOList.size());

        for (Task task : tasksFromFeature) {
            Optional<TaskDTO> testOptional = taskDTOList.stream()
                    .filter(taskDTO -> Objects.equals(taskDTO.getId(), task.getId()) && taskDTO.getQuestion().equals(task.getQuestion())).findFirst();
            assertTrue(testOptional.isPresent());
        }

    }
}
