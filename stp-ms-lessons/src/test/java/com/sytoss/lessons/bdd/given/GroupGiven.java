package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Optional;

public class GroupGiven extends CucumberIntegrationTest {

    @Given("^groups exist$")
    public void groupsExist(List<GroupDTO> groups) {
        for (GroupDTO groupDTO : groups) {

            Optional<TeacherDTO> optionalTeacherDTO = getTeacherConnector().findById(TestExecutionContext.getTestContext().getTeacherId());
            TeacherDTO teacherDTO = optionalTeacherDTO.orElse(null);

            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(groupDTO.getDiscipline().getName(), teacherDTO.getId());
            if (disciplineDTO == null) {
                disciplineDTO = groupDTO.getDiscipline();
                disciplineDTO.setTeacher(teacherDTO);
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }
            TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
            GroupDTO result = getGroupConnector().getByNameAndDisciplineId(groupDTO.getName(), disciplineDTO.getId());
            groupDTO.setDiscipline(disciplineDTO);
            if (result == null) {
                getGroupConnector().save(groupDTO);
            }
        }
    }

    @Given("^\"(.*)\" group does not exist for this discipline$")
    public void groupDoesNotExistForDiscipline(String groupName) {
        GroupDTO groupDTO = getGroupConnector().getByNameAndDisciplineId(groupName, TestExecutionContext.getTestContext().getDisciplineId());
        if (groupDTO != null) {
            getGroupConnector().delete(groupDTO);
        }
    }

    @Given("^\"(.*)\" group exist for this discipline$")
    public void groupExistForDiscipline(String groupName) {
        GroupDTO groupDTO = getGroupConnector().getByNameAndDisciplineId(groupName, TestExecutionContext.getTestContext().getDisciplineId());
        if (groupDTO == null){
            groupDTO = new GroupDTO();
            groupDTO.setName(groupName);
            groupDTO.setDiscipline(getDisciplineConnector().getReferenceById(TestExecutionContext.getTestContext().getDisciplineId()));
            getGroupConnector().save(groupDTO);
        }
    }
}


