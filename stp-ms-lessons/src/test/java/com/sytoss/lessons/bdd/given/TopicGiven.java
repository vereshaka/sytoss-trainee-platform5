package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
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
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(topic.getDiscipline().getName());
            if (disciplineDTO == null) {
                disciplineDTO = getDisciplineConnector().save(topic.getDiscipline());
            }
            TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
            TopicDTO topicResult = getTopicConnector().getByNameAndDisciplineId(topic.getName(), disciplineDTO.getId());
            topic.setDiscipline(disciplineDTO);
            if (topicResult == null) {
                getTopicConnector().save(topic);
            }
        }
    }

    @Given("^\"(.*)\" topic by \"(.*)\" discipline doesn't exist$")
    public void topicExist(String topicName, String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        TopicDTO topic = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
        if (topic != null) {
            getTopicConnector().delete(topic);
        }
    }
}
