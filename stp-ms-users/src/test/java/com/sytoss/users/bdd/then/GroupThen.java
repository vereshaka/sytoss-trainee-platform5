package com.sytoss.users.bdd.then;

import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupThen extends CucumberIntegrationTest {


    @Then("^this student should have \"(.*)\" group$")
    public void groupShouldBe(String groupName) {
        UserDTO studentDTO = TestExecutionContext.getTestContext().getUser();
        assertNotNull(studentDTO);
        TestExecutionContext.getTestContext().getResponse().getBody().
        assertEquals(disciplineName, studentDTO.ge);
    }
}
