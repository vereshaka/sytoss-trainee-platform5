package com.sytoss.users.bdd.given;

import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.dto.StudentDTO;
import io.cucumber.java.en.Given;

public class StudentGiven extends CucumberIntegrationTest {

    @Given("^student with \"(.*)\" firstName and \"(.*)\" middleName and \"(.*)\" lastName and \"(.*)\" email exists$")
    public void studentExists(String firstName, String middleName, String lastName, String email) {
        StudentDTO studentDTO = getStudentConnector().getByEmail(email);
        if (studentDTO != null) {
            studentDTO = new StudentDTO();
            studentDTO.setFirstName(firstName);
            studentDTO.setMiddleName(middleName);
            studentDTO.setLastName(lastName);
            studentDTO.setEmail(email);
            studentDTO.setModerated(false);
            getStudentConnector().save(studentDTO);
        }
    }
}