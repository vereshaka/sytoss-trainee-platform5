package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;
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
}


