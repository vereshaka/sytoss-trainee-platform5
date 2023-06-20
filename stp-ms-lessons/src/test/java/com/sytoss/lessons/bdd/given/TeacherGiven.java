package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TeacherGiven extends CucumberIntegrationTest {

    @Given("^teacher \"(.*)\" \"(.*)\" with \"(.*)\" email exists$")
    public void teacherExists(String firstName, String lastName, String email) {
        TestExecutionContext.getTestContext().setTeacherId(0L);
        TestExecutionContext.getTestContext().setToken(generateJWT(new ArrayList<>(), firstName, lastName, email, "teacher"));
    }

    @Given("^teachers have groups")
    public void teachersHaveGroups(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        getListOfGroupsFromDataTable(rows);
    }

    private void getListOfGroupsFromDataTable(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            String index = columns.get("teacherId");
            Long teacherId = TestExecutionContext.getTestContext().getIdMapping().get(index);
            if(teacherId == null){
                teacherId = 10L;
                if(!TestExecutionContext.getTestContext().getIdMapping().isEmpty()){
                    Long maxId = Collections.max(TestExecutionContext.getTestContext().getIdMapping().entrySet(),
                            Map.Entry.comparingByValue()).getValue();
                    teacherId = maxId+1;
                }
                TestExecutionContext.getTestContext().getIdMapping().put(index,teacherId);
            }

            String disciplineName = columns.get("discipline");
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, teacherId);
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setName(disciplineName);
                disciplineDTO.setTeacherId(teacherId);
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }

            String groupName = columns.get("group");
            GroupDTO groupDTO = getGroupConnector().getByNameAndDisciplineId(disciplineName, disciplineDTO.getId());
            if (groupDTO == null) {
                groupDTO = new GroupDTO();
                groupDTO.setName(groupName);
                groupDTO.setDiscipline(disciplineDTO);
                getGroupConnector().save(groupDTO);
            }
        }
    }
}
