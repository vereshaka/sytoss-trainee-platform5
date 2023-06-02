package com.sytoss.users.bdd.then;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TeacherThen extends CucumberIntegrationTest {

    @Then("^teacher with \"(.*)\" firstname and \"(.*)\" middlename and \"(.*)\" lastname should exist$")
    public void disciplineShouldBeReceived(String firstName, String middlename, String lastname) {
        Teacher teacher = (Teacher) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(middlename, teacher.getMiddleName());
        assertEquals(lastname, teacher.getLastName());
    }
}
