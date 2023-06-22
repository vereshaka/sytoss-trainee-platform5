package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupGiven extends CucumberIntegrationTest {

//    @Given("^groups exist$")
//    public void groupsExist(List<GroupReferenceDTO> groups) {
//        for (GroupReferenceDTO groupReferenceDTO : groups) {
//            Optional<DisciplineDTO> optionalDisciplineDTO = getDisciplineConnector().findById(groupReferenceDTO.getDisciplineId());
//            DisciplineDTO disciplineDTO = optionalDisciplineDTO.orElse(null);
//            if (disciplineDTO == null) {
//                disciplineDTO = new DisciplineDTO();
//                disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
//                getDisciplineConnector().save(disciplineDTO);
//            }
//            GroupReferenceDTO result = getGroupReferenceConnector().findByGroupId(groupReferenceDTO.getGroupId());
//            if (result == null) {
//                getGroupReferenceConnector().save(groupReferenceDTO);
//            }
//        }
//    }

    @Given("^this teacher has \"(.*)\" discipline with id (.*) contains groups with id$")
    public void groupsExist(String disciplineName, String disciplineId, DataTable dataTable) {

        List<Long> groupIds = dataTable.asList(Long.class);
        if (TestExecutionContext.getTestContext().getIdMapping().get(disciplineId) != null) {
            Optional<DisciplineDTO> optionalDisciplineDTO = getDisciplineConnector().findById(Long.valueOf(disciplineId));
            DisciplineDTO disciplineDTO = optionalDisciplineDTO.orElse(null);
            getDisciplineConnector().delete(disciplineDTO);
        }

        Optional<DisciplineDTO> optionalDisciplineDTO = getDisciplineConnector().findById(Long.valueOf(disciplineId));
        DisciplineDTO disciplineDTO = optionalDisciplineDTO.orElse(null);
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
            disciplineDTO = getDisciplineConnector().save(disciplineDTO);
        }

        List<GroupReferenceDTO> groupReferenceDTOS = new ArrayList<>();

        for (Long groupId : groupIds) {
            GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(groupId, disciplineDTO.getId());
            groupReferenceDTOS.add(groupReferenceDTO);
            getGroupReferenceConnector().save(groupReferenceDTO);
        }

        disciplineDTO.setGroupReferences(groupReferenceDTOS);

        getDisciplineConnector().save(disciplineDTO);

        TestExecutionContext.getTestContext().registerId("disciplineId", disciplineDTO.getId());
    }
}