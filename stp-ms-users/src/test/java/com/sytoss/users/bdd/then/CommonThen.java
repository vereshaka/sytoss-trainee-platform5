package com.sytoss.users.bdd.then;


import com.sytoss.users.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonThen {

    @Then("^operation is successful$")
    public void status() {
        Assertions.assertEquals(200, TestExecutionContext.getTestContext().getResponse().getStatusCode().value());
    }

    @Then("^operation should be finished with (\\w+) \"(.*)\" error$")
    public void raiseError(Integer status, String error) {
        assertEquals(status, TestExecutionContext.getTestContext().getResponse().getStatusCode().value());
        assertEquals(error, TestExecutionContext.getTestContext().getResponse().getBody());
    }
}
