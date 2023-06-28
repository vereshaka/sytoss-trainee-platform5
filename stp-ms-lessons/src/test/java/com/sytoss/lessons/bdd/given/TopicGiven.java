package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TopicGiven extends AbstractGiven {

    @Given("^topic with id (.*) contains the following tasks:")
    public void topicContainsTasks(String topicKey, DataTable tasksData) {
        TopicDTO topic = getTopicConnector().getReferenceById(TestExecutionContext.getTestContext().getIdMapping().get(topicKey));
        List<TaskDTO> existsTasks = getTaskConnector().findByTopicsId(topic.getId());

        TaskDomainDTO taskDomain = getTaskDomainConnector().getReferenceById(TestExecutionContext.getTestContext().getTaskDomainId());

        List<String> tasks = new ArrayList<>();

        for (int i = 1; i < tasksData.height(); i++) {
            String taskName = tasksData.row(i).get(0).trim();
            tasks.add(taskName);
            TaskDTO result = existsTasks.stream().filter(item -> item.getQuestion().equalsIgnoreCase(taskName)).findFirst().orElse(null);
            if (result == null) {
                result = new TaskDTO();
                result.setQuestion(taskName);
                result.setTopics(List.of(topic));
                result.setTaskDomain(taskDomain);
                getTaskConnector().save(result);
            }
        }
        deleteTasks(existsTasks.stream().filter(item -> !tasks.contains(item.getQuestion().toLowerCase())).toList());
    }

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

    @Given("^topic \"(.*)\" exists$")
    public void topicExists(String topicName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(TestExecutionContext.getTestContext().getDisciplineId());
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
        if (topicDTO == null) {
            topicDTO = new TopicDTO();
            topicDTO.setName(topicName);
            topicDTO.setDiscipline(disciplineDTO);
            topicDTO = getTopicConnector().save(topicDTO);
        }
        TestExecutionContext.getTestContext().setTopicId(topicDTO.getId());
    }

    @Given("^this topic has icon with bytes \"([^\\\"]*)\"$")
    public void topicHasIcon(String iconBytes) {
        String[] numberStrings = iconBytes.split(", ");
        byte[] icon = new byte[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            icon[i] = Byte.parseByte(numberStrings[i]);
        }
        Optional<TopicDTO> optionalTopicDTO = getTopicConnector().findById(TestExecutionContext.getTestContext().getTopicId());
        TopicDTO topicDTO = optionalTopicDTO.orElse(null);
        topicDTO.setIcon(icon);
        getTopicConnector().save(topicDTO);
    }
}
