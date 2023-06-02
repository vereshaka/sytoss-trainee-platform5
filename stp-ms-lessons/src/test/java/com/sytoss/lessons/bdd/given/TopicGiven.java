package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.*;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TopicGiven extends CucumberIntegrationTest {

    @Given("topic exist")
    public void thisExamHasAnswers(List<TopicDTO> topics) {

        for (TopicDTO topic : topics) {
            Optional<TeacherDTO> optionalTeacherDTO = getTeacherConnector().findById(TestExecutionContext.getTestContext().getTeacherId());
            TeacherDTO teacherDTO = optionalTeacherDTO.orElse(null);

            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(topic.getDiscipline().getName(), teacherDTO.getId());
            if (disciplineDTO == null) {
                disciplineDTO = topic.getDiscipline();
                disciplineDTO.setTeacher(teacherDTO);
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

    @Given("^This discipline has \"(.*)\" project$")
    public void customerHasProject(String topicName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(TestExecutionContext.getTestContext().getDisciplineId());
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, TestExecutionContext.getTestContext().getDisciplineId());
        if (topicDTO == null) {
            topicDTO = new TopicDTO();
            topicDTO.setName(topicName);
            topicDTO.setDiscipline(disciplineDTO);
            topicDTO = getTopicConnector().save(topicDTO);
        }
        TestExecutionContext.getTestContext().setTopicId(topicDTO.getId());
    }

    @Given("^\"(.*)\" topic by \"(.*)\" discipline doesn't exist$")
    public void topicExist(String topicName, String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        TopicDTO topic = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
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
