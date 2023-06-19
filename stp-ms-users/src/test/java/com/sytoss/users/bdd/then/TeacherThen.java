package com.sytoss.users.bdd.then;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeacherThen extends CucumberIntegrationTest {

    @Then("^teacher with \"(\\w+)\" firstname and \"(\\w+)\" lastname should exist$")
    public void teacherShouldBeSave(String firstName, String lastname) {
        Teacher teacher = (Teacher) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(lastname, teacher.getLastName());
    }
}
