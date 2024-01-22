package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import jakarta.transaction.Transactional;

import java.sql.*;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
public class TopicGiven extends AbstractGiven {

    @Given("^topic with specific id (.*) exists")
    public void topicWithIdExists(Long topicId) {
        try {
            Connection connection = getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM TOPIC WHERE ID = " + topicId);
            statement.execute("INSERT INTO TOPIC (ID, NAME, DISCIPLINE_ID) VALUES(" + topicId + ", 'Generic Topic#" + topicId + "', " +
                    getTestExecutionContext().getDetails().getDisciplineId() + ")");
            while (true) {
                ResultSet rs = statement.executeQuery("select TOPIC_SEQ.nextVal from Dual");
                rs.next();
                int id = rs.getInt(1);
                if (id >= topicId) {
                    break;
                }
            }
            statement.close();
            connection.commit();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM TOPIC where ID = " + topicId);
            assertTrue(rs.next());
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getEntityManager().clear();
    }

    @Given("^topic with id (.*) contains the following tasks:")
    public void topicContainsTasks(String topicKey, DataTable tasksData) {
        TopicDTO topic = getTopicConnector().getReferenceById((Long) getTestExecutionContext().getIdMapping().get(topicKey));
        List<TaskDTO> existsTasks = getTaskConnector().findByTopicsIdOrderByCode(topic.getId());

        TaskDomainDTO taskDomain = getTaskDomainConnector().getReferenceById(getTestExecutionContext().getDetails().getTaskDomainId());

        List<String> tasks = new ArrayList<>();

        for (int i = 1; i < tasksData.height(); i++) {
            String taskName = tasksData.row(i).get(0).trim();
            String taskCode = tasksData.row(i).get(1).trim();
            tasks.add(taskName);
            TaskDTO result = existsTasks.stream().filter(item -> item.getQuestion().equalsIgnoreCase(taskName)).findFirst().orElse(null);
            if (result == null) {
                result = new TaskDTO();
                result.setMode("AND");
                result.setQuestion(taskName);
                result.setTopics(List.of(topic));
                result.setCode(taskCode);
                result.setTaskDomain(taskDomain);
                getTaskConnector().save(result);
            }
        }
        deleteTasks(existsTasks.stream().filter(item -> !tasks.contains(item.getQuestion().toLowerCase())).toList());
    }

    @Given("^topics exist")
    public void thisExamHasAnswers(List<Topic> topics) {
        Long teacherId = getTestExecutionContext().getDetails().getTeacherId();
        List<TopicDTO> topicDTOS = new ArrayList<>();
        for (Topic topic : topics) {
            TopicDTO topicDTO = new TopicDTO();
            topicDTO.setName(topic.getName());
            DisciplineDTO disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(topic.getDiscipline().getName());
            topicDTO.setDiscipline(disciplineDTO);
            topicDTOS.add(topicDTO);
        }
        Map<Long, List<Long>> finalTopics = new HashMap<>();
        for (TopicDTO topic : topicDTOS) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(topic.getDiscipline().getName(), teacherId);
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setName(topic.getDiscipline().getName());
                disciplineDTO.setTeacherId(teacherId);
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }
            getTestExecutionContext().getDetails().setDisciplineId(disciplineDTO.getId());
            TopicDTO topicResult = getTopicConnector().getByNameAndDisciplineId(topic.getName(), disciplineDTO.getId());
            if (topicResult == null) {
                topicResult = new TopicDTO();
                topicResult.setName(topic.getName());
                topicResult.setDiscipline(disciplineDTO);
                topicResult = getTopicConnector().save(topicResult);

            }
            List<Long> topicIds = finalTopics.get(disciplineDTO.getId());
            if (topicIds == null) {
                topicIds = new ArrayList<>();
                finalTopics.put(disciplineDTO.getId(), topicIds);
            }
            topicIds.add(topicResult.getId());
        }
        for (Map.Entry<Long, List<Long>> entry : finalTopics.entrySet()) {
            List<TopicDTO> topicDTOSFromConnector = getTopicConnector().findByDisciplineIdOrderByName(entry.getKey());
            Iterator<TopicDTO> iTopic = topicDTOSFromConnector.iterator();
            while (iTopic.hasNext()) {
                TopicDTO t = iTopic.next();
                if (!entry.getValue().contains(t.getId())) {
                    getTopicConnector().deleteById(t.getId());
                    iTopic.remove();
                }
            }
        }
    }

    @Given("^this discipline doesn't have \"(.*)\" topic$")
    public void topicExist(String topicName) {
        TopicDTO topic = getTopicConnector().getByNameAndDisciplineId(topicName, getTestExecutionContext().getDetails().getDisciplineId());
        if (topic != null) {
            getTopicConnector().delete(topic);
        }
    }

    @Given("^this discipline has \"(.*)\" topic$")
    public void disciplineHasTopic(String topicName) {

        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());

        TopicDTO topicResult = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
        if (topicResult == null) {
            topicResult = new TopicDTO();
            topicResult.setName(topicName);
            topicResult.setDiscipline(disciplineDTO);
            topicResult = getTopicConnector().save(topicResult);
        }
        getTestExecutionContext().getDetails().setTopicId(topicResult.getId());
    }

    @Given("^topic \"(.*)\" exists$")
    public void topicExists(String topicName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
        if (topicDTO == null) {
            topicDTO = new TopicDTO();
            topicDTO.setName(topicName);
            topicDTO.setDiscipline(disciplineDTO);
            topicDTO = getTopicConnector().save(topicDTO);
        }
        getTestExecutionContext().getDetails().setTopicId(topicDTO.getId());
    }

    @Given("^this topic has icon with bytes \"([^\\\"]*)\"$")
    public void topicHasIcon(String iconBytes) {
        String[] numberStrings = iconBytes.split(", ");
        byte[] icon = new byte[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            icon[i] = Byte.parseByte(numberStrings[i]);
        }
        Optional<TopicDTO> optionalTopicDTO = getTopicConnector().findById(getTestExecutionContext().getDetails().getTopicId());
        TopicDTO topicDTO = optionalTopicDTO.orElse(null);
        topicDTO.setIcon(icon);
        getTopicConnector().save(topicDTO);
    }
}
