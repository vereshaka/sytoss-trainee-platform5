package com.sytoss.users.bdd.common;

import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.StudentDTO;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableCommon extends UsersIntegrationTest {

    @DataTableType
    public StudentDTO mapStudents(Map<String, String> entry) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName(entry.get("firstName"));
        studentDTO.setLastName(entry.get("lastName"));
        return studentDTO;
    }
}
