package com.sytoss.users.bdd.given;

import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.dto.TeacherDTO;
import io.cucumber.java.en.Given;

public class TeacherGiven extends CucumberIntegrationTest {

    @Given("^teacher with \"(.*)\" firstname and \"(.*)\" middlename and \"(.*)\" lastname doesnt exist$")
    public void teacherNotExist(String firstName, String middlename, String lastname) {
        TeacherDTO teacherDTO = getTeacherConnector().getByFirstNameAndMiddleNameAndLastName(firstName, middlename,lastname);
        if (teacherDTO != null) {
            getTeacherConnector().delete(teacherDTO);
        }
    }
}
