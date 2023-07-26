package com.sytoss.users.bdd.given;

import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

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
    public void thisGroupHasStudents(String groupName, List<StudentDTO> studentDTOList) {
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        groupDTO.setStudents(studentDTOList);
        for(StudentDTO studentDTO : studentDTOList){
            getUserConnector().save(studentDTO);
        }
        getGroupConnector().save(groupDTO);
    }
}