package com.sytoss.users.bdd.given;

import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
public class UserGiven extends CucumberIntegrationTest {

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

    @Given("^student with \"(.*)\" firstName and \"(.*)\" lastName and \"(.*)\" email exists$")
    public void studentExists(String firstName, String lastName, String email) {
        UserDTO studentDTO = getUserConnector().getByEmail(email);
        if (studentDTO != null && !(studentDTO instanceof StudentDTO)) {
            getUserConnector().delete(studentDTO);
            studentDTO = null;
        }
        if (studentDTO == null) {
            studentDTO = new StudentDTO();
            studentDTO.setFirstName(firstName);
            studentDTO.setLastName(lastName);
            studentDTO.setEmail(email);
            studentDTO.setModerated(false);
            studentDTO = getUserConnector().save(studentDTO);
        }
        TestExecutionContext.getTestContext().setUser(studentDTO);
    }

    @Given("this student assign to group")
    public void thisStudentAssignToGroup(List<GroupDTO> groupDTOList) {
        StudentDTO studentDTO = (StudentDTO) getUserConnector().getByEmail(TestExecutionContext.getTestContext().getUser().getEmail());
        groupDTOList.forEach(getGroupConnector()::save);

        studentDTO.setGroups(groupDTOList);
        getUserConnector().save(studentDTO);
    }

    @Given("^this student has photo with bytes \"([^\\\"]*)\"$")
    public void userHasPhoto(String photoBytes) {
        String[] numberStrings = photoBytes.split(", ");
        byte[] icon = new byte[numberStrings.length];
        for (int i = 0; i < numberStrings.length; i++) {
            icon[i] = Byte.parseByte(numberStrings[i]);
        }
        Optional<UserDTO> optionalDisciplineDTO = getUserConnector().findById(TestExecutionContext.getTestContext().getUser().getId());
        UserDTO studentDTO = optionalDisciplineDTO.orElse(null);
        studentDTO.setPhoto(icon);
        getUserConnector().save(studentDTO);
    }
}
