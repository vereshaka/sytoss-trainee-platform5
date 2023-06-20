package com.sytoss.users.bdd.given;

import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.en.Given;
import jakarta.transaction.Transactional;

@Transactional
public class UserGiven extends CucumberIntegrationTest {

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
}
