package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.Given;

import java.util.List;

public class TopicGiven extends CucumberIntegrationTest {

    @Given("topics exist")
    public void thisExamHasAnswers(List<TopicDTO> topics) {

        for (TopicDTO topic : topics) {
            Long teacherId = TestExecutionContext.getTestContext().getTeacherId();
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(topic.getDiscipline().getName(), teacherId);
            if (disciplineDTO == null) {
                disciplineDTO = topic.getDiscipline();
                disciplineDTO.setTeacherId(teacherId);
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }
            TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
            TopicDTO topicResult = getTopicConnector().getByNameAndDisciplineId(topic.getName(), disciplineDTO.getId());
            topic.setDiscipline(disciplineDTO);
            if (topicResult == null) {
                topicResult = getTopicConnector().save(topic);
            }
            TestExecutionContext.getTestContext().setTopicId(topicResult.getId());
        }
    }

    @Given("^this discipline doesn't have \"(.*)\" topic$")
    public void topicExist(String topicName) {
        TopicDTO topic = getTopicConnector().getByNameAndDisciplineId(topicName, TestExecutionContext.getTestContext().getDisciplineId());
        if (topic != null) {
            getTopicConnector().delete(topic);
        }
    }

    @Given("^this discipline has \"(.*)\" topic$")
    public void disciplineHasTopic(String topicName) {

        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(TestExecutionContext.getTestContext().getDisciplineId());

        TopicDTO topicResult = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
        if (topicResult == null) {
            topicResult = new TopicDTO();
            topicResult.setName(topicName);
            topicResult.setDiscipline(disciplineDTO);
            topicResult = getTopicConnector().save(topicResult);
        }
        TestExecutionContext.getTestContext().setTopicId(topicResult.getId());
    }
}
