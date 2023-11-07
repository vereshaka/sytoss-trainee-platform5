package com.sytoss.users.bdd.common;

import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableConfigurator extends UsersIntegrationTest {

    @DataTableType
    public GroupDTO mapGroupDTO(Map<String, String> row) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName(row.get("group"));
        return groupDTO;
    }

    @DataTableType
    public StudentDTO mapStudentDTO(Map<String, String> row) {
        StudentDTO result = new StudentDTO();
        result.setFirstName(row.get("firstName"));
        result.setLastName(row.get("lastName"));
        return result;
    }
}
