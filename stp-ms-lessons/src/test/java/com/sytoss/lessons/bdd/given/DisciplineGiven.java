package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DisciplineGiven extends AbstractGiven {

    @Given("^this teacher has \"(.*)\" discipline with id (.*) and following topics:")
    public void teacherHasDiscipline(String disciplineName, String disciplineId, DataTable topicsData) {
        Long teacherId = TestExecutionContext.getTestContext().getTeacherId();
        DisciplineDTO discipline = getDisciplineConnector().getByNameAndTeacherId(disciplineName, teacherId);
        if (discipline == null) {
            discipline = new DisciplineDTO();
            discipline.setName(disciplineName);
            discipline.setTeacherId(teacherId);
            discipline = getDisciplineConnector().save(discipline);
        }
        TestExecutionContext.getTestContext().setDisciplineId(discipline.getId());
        TestExecutionContext.getTestContext().registerId(disciplineId, discipline.getId());

        List<String> topics = new ArrayList<>();
        List<TopicDTO> existTopics = getTopicConnector().findByDisciplineId(discipline.getId());

        for (int i = 1; i < topicsData.height(); i++) {
            String topicKey = topicsData.row(i).get(0).trim();
            String topicName = topicsData.row(i).get(1).trim();
            topics.add(topicName);
            TopicDTO result = existTopics.stream().filter(item -> item.getName().equalsIgnoreCase(topicName)).findFirst().orElse(null);
            if (result == null) {
                result = new TopicDTO();
                result.setName(topicName);
                result.setDiscipline(discipline);
                result = getTopicConnector().save(result);
            }
            TestExecutionContext.getTestContext().registerId(topicKey, result.getId());
        }
        deleteTopics(existTopics.stream().filter(item -> !topics.contains(item.getName().toLowerCase())).toList());
    }

    @Given("disciplines exist")
    public void disciplinesExist(List<DisciplineDTO> disciplines) {
        List<DisciplineDTO> disciplineDTOList = getDisciplineConnector().findAll();
        for (DisciplineDTO disciplineDTO : disciplineDTOList) {
            for (DisciplineDTO disciplineDtoFromTable : disciplines) {
                if (!(disciplineDTO.getName().equals(disciplineDtoFromTable.getName()))) {
                    getDisciplineConnector().deleteById(disciplineDTO.getId());
                }
                if (!(disciplineDTO.getTeacherId().equals(disciplineDtoFromTable.getTeacherId()))) {
                    getDisciplineConnector().deleteById(disciplineDTO.getId());
                }
            }
        }
        for (DisciplineDTO discipline : disciplines) {
            DisciplineDTO disciplineResult = getDisciplineConnector().getByNameAndTeacherId(discipline.getName(), discipline.getTeacherId());
            if (disciplineResult == null) {
                getDisciplineConnector().save(discipline);
            }
        }
    }

    @Given("^\"(.*)\" discipline exists for this teacher$")
    public void disciplineExistsForTeacher(String nameDiscipline) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(nameDiscipline,TestExecutionContext.getTestContext().getTeacherId());
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(nameDiscipline);
            disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
            disciplineDTO = getDisciplineConnector().save(disciplineDTO);
        }
        TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
    }

    @Given("^discipline \"(.*)\" doesn't exist$")
    public void disciplineNotExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        if (disciplineDTO != null) {
            getDisciplineConnector().delete(disciplineDTO);
        }
    }

    @Given("^\"(.*)\" discipline exists$")
    public void disciplineExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
            disciplineDTO =  getDisciplineConnector().save(disciplineDTO);
        }
        TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
    }
}
