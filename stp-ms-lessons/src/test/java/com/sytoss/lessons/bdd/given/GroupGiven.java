package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Optional;

public class GroupGiven extends CucumberIntegrationTest {

    @Given("^groups exist$")
    public void groupsExist(List<GroupReferenceDTO> groups) {
        for (GroupReferenceDTO groupReferenceDTO : groups) {
            Optional<DisciplineDTO> optionalDisciplineDTO = getDisciplineConnector().findById(groupReferenceDTO.getDisciplineId());
            DisciplineDTO disciplineDTO = optionalDisciplineDTO.orElse(null);
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
                getDisciplineConnector().save(disciplineDTO);
            }
            GroupReferenceDTO result = getGroupReferenceConnector().findByGroupId(groupReferenceDTO.getGroupId());
            if (result == null) {
                getGroupReferenceConnector().save(groupReferenceDTO);
            }
        }
    }

//    @Given("^\"(.*)\" group does not exist for this discipline$")
//    public void groupDoesNotExistForDiscipline(String groupName) {
//        GroupDTO groupDTO = getGroupConnector().getByNameAndDisciplineId(groupName, TestExecutionContext.getTestContext().getDisciplineId());
//        if (groupDTO != null) {
//            getGroupConnector().delete(groupDTO);
//        }
//    }
//
//    @Given("^\"(.*)\" group exist for this discipline$")
//    public void groupExistForDiscipline(String groupName) {
//        GroupDTO groupDTO = getGroupConnector().getByNameAndDisciplineId(groupName, TestExecutionContext.getTestContext().getDisciplineId());
//        if (groupDTO == null) {
//            groupDTO = new GroupDTO();
//            groupDTO.setName(groupName);
//            groupDTO.setDiscipline(getDisciplineConnector().getReferenceById(TestExecutionContext.getTestContext().getDisciplineId()));
//            getGroupConnector().save(groupDTO);
//        }
//    }
}