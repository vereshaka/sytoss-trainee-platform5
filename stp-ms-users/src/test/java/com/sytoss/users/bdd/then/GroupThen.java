package com.sytoss.users.bdd.then;

import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupThen extends CucumberIntegrationTest {

    @Then("^this student should have \"(.*)\" group$")
    public void studentShouldHasGroup(String groupName) {
        Student student = (Student) TestExecutionContext.getTestContext().getResponse().getBody();
        assertNotNull(student);
        assertEquals(groupName, student.getPrimaryGroup().getName());
    }
}
