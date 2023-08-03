package com.sytoss.users.bdd.given;

import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public class UserGiven extends UsersIntegrationTest {

    @DataTableType
    public GroupDTO mapGroupDTO(Map<String, String> row) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName(row.get("group"));
        return groupDTO;
    }

    @Given("^teacher with \"(\\w+)\" firstName and \"(\\w+)\" lastName doesnt exist$")
    public void teacherNotExist(String firstName, String lastName) {
        UserDTO teacherDTO = getUserConnector().getByFirstNameAndLastName(firstName, lastName);
        if (teacherDTO != null) {
            getUserConnector().delete(teacherDTO);
        }
    }

    @Given("^student with \"(.*)\" firstName, \"(.*)\" middleName and \"(.*)\" lastName and \"(.*)\" email exists$")
    public void studentExists(String firstName, String middleName, String lastName, String email) {
        String id = "thisIsNotLongId";
        UserDTO studentDTO = getUserConnector().getByUid(id);
        if (studentDTO != null && !(studentDTO instanceof StudentDTO)) {
            getUserConnector().delete(studentDTO);
            studentDTO = null;
        }
        if (studentDTO == null) {
            studentDTO = new StudentDTO();
            studentDTO.setFirstName(firstName);
            studentDTO.setMiddleName(middleName);
            studentDTO.setLastName(lastName);
            studentDTO.setEmail(email);
            studentDTO.setModerated(false);
            studentDTO.setUid("thisIsNotLongId");
            studentDTO = getUserConnector().save(studentDTO);
        }
        getTestExecutionContext().getDetails().setUser(studentDTO);
    }

    @Given("this student assign to group")
    public void thisStudentAssignToGroup(List<GroupDTO> groupDTOList) {
        StudentDTO studentDTO = (StudentDTO) getUserConnector().getByEmail(getTestExecutionContext().getDetails().getUser().getEmail());
        groupDTOList.forEach(getGroupConnector()::save);

        studentDTO.setGroups(groupDTOList);
        getUserConnector().save(studentDTO);
    }

    @Given("this user has profile photo")
    public void thisUserHasProfilePhoto() {
        UserDTO userDTO = getTestExecutionContext().getDetails().getUser();
        byte[] photoBytes = {0x01, 0x02, 0x03};
        userDTO.setPhoto(photoBytes);
        getUserConnector().save(userDTO);
    }

    @Given("^this student has photo with bytes \"([^\\\"]*)\"$")
    public void userHasPhoto(String photoBytes) {
        String[] numberStrings = photoBytes.split(", ");
        byte[] icon = new byte[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            icon[i] = Byte.parseByte(numberStrings[i]);
        }
        UserDTO studentDTO = getUserConnector().getByUid(getTestExecutionContext().getDetails().getUser().getUid());
        studentDTO.setPhoto(icon);
        getUserConnector().save(studentDTO);
    }
}
