package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.IntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

public class TopicGiven extends CucumberIntegrationTest {

    @DataTableType
    public TopicDTO mapTopic(Map<String, String> entry) {
        TopicDTO topic = new TopicDTO();
        DisciplineDTO discipline = new DisciplineDTO();
        topic.setName(entry.get("topic"));
        discipline.setName(entry.get("discipline"));
        topic.setDiscipline(discipline);
        return topic;
    }

    @Given("topic exist")
    public void thisExamHasAnswers(List<TopicDTO> topics) {
        for (TopicDTO topic : topics) {
            TopicDTO topicResult = getTopicConnector().getByName(topic.getName());
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(topic.getDiscipline().getName());
            if (disciplineDTO == null) {
                disciplineDTO = getDisciplineConnector().save(topic.getDiscipline());
            }
            IntegrationTest.getTestContext().setDisciplineId(disciplineDTO.getId());
            topic.setDiscipline(disciplineDTO);
            if (topicResult == null) {
                getTopicConnector().save(topic);
            }
        }
    }
}
