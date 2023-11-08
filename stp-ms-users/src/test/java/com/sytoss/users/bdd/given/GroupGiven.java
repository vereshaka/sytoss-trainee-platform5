package com.sytoss.users.bdd.given;

import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;

public class GroupGiven extends UsersIntegrationTest {

    @Given("^\"(.*)\" group exists$")
    public void groupExist(String groupName) {
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        if (groupDTO == null) {
            groupDTO = new GroupDTO();
            groupDTO.setName(groupName);
            getGroupConnector().save(groupDTO);
        }
    }

    @Given("^\"(.*)\" group doesnt exist$")
    public void groupNotExist(String groupName) {
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        if (groupDTO != null) {
            getUserConnector().deleteAll();
            getGroupConnector().delete(groupDTO);
        }
    }

    @Given("^\"(.*)\" group has students")
    public void thisGroupHasStudents(String groupName, List<Student> studentList) {
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        List<StudentDTO> studentDTOList = new ArrayList<>();
        for (Student student : studentList) {
            StudentDTO studentDTO = new StudentDTO();
            getUserConverter().toDTO(student, studentDTO);
            studentDTOList.add(studentDTO);
        }
        groupDTO.setStudents(studentDTOList);
        for (StudentDTO studentDTO : studentDTOList) {
            getUserConnector().save(studentDTO);
        }
        getGroupConnector().save(groupDTO);
    }
}