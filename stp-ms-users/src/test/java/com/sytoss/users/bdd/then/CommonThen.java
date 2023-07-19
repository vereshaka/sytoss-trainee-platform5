package com.sytoss.users.bdd.then;

import com.sytoss.users.bdd.UsersIntegrationTest;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonThen extends UsersIntegrationTest {

    @Then("^operation is successful$")
    public void status() {
        Assertions.assertEquals(200, getTestExecutionContext().getResponse().getStatusCode().value());
    }

    @Then("^operation should be finished with (\\w+) \"(.*)\" error$")
    public void raiseError(Integer status, String error) {
        assertEquals(status, getTestExecutionContext().getResponse().getStatusCode().value());
        //assertEquals(error, getTestExecutionContext().getDetails().getResponse().getBody());
        // TODO: 05.07.2023 LarinI: Find a way to check an error message 
    }
}
