package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.Given;

import java.util.List;

public class ExamGiven extends CucumberIntegrationTest {

    @Given("topics exist")
    public void topicExists(List<TopicDTO> topicDTOList) {
        topicDTOList.forEach(topicDTO -> {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(topicDTO.getDiscipline().getName());

            if (disciplineDTO == null) {
                disciplineDTO = getDisciplineConnector().save(topicDTO.getDiscipline());
            }

            TopicDTO result = getTopicConnector().getByNameAndDisciplineId(topicDTO.getName(), disciplineDTO.getId());

            if (result == null) {
                topicDTO.setDiscipline(disciplineDTO);
                getTopicConnector().save(topicDTO);
            }
        });
    }

    @Given("\"{word}\" discipline has \"{word}\" group")
    public void disciplineHasGroup(String disciplineName, String groupName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName(groupName);
        groupDTO.setDiscipline(disciplineDTO);

        getGroupConnector().save(groupDTO);
    }
}
