package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.when;

public class TeacherGiven extends LessonsIntegrationTest {

    @Given("^teacher \"(.*)\" \"(.*)\" with \"(.*)\" email exists$")
    public void teacherExists(String firstName, String lastName, String email) {
        Long teacherId = 0L;
        getTestExecutionContext().getDetails().setTeacherId(teacherId);
        getTestExecutionContext().setToken(generateJWT(new ArrayList<>(), firstName, lastName, email, "teacher"));
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", getTestExecutionContext().getDetails().getTeacherId().intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);
    }

    @Given("^teachers have groups")
    public void teachersHaveGroups(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> columns : rows) {
            Long teacherId = Long.valueOf(columns.get("teacherId"));

            String disciplineName = columns.get("discipline");
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, teacherId);
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setName(disciplineName);
                disciplineDTO.setTeacherId(teacherId);
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }

            Long groupId = Long.parseLong(columns.get("group"));
            GroupReferenceDTO groupDTO = getGroupReferenceConnector().findByGroupId(groupId);
            if (groupDTO == null) {
                groupDTO = new GroupReferenceDTO();
                groupDTO.setGroupId(groupId);
                groupDTO.setDiscipline(disciplineDTO);
                getGroupReferenceConnector().save(groupDTO);
            }
        }
    }
}
