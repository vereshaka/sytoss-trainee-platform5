package com.sytoss.users.bdd.given;

import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.en.Given;

public class TeacherGiven extends CucumberIntegrationTest {

    @Given("^teacher with \"(.*)\" firstName and \"(.*)\" middleName and \"(.*)\" lastName doesnt exist$")
    public void teacherNotExist(String firstName, String middleName, String lastName) {
        UserDTO teacherDTO = getTeacherConnector().getByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);
        if (teacherDTO != null) {
            getTeacherConnector().delete(teacherDTO);
        }
    }
}
