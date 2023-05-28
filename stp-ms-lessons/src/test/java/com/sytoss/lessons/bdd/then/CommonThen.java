package com.sytoss.lessons.bdd.then;

import com.sytoss.lessons.bdd.common.IntegrationTest;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonThen {

    @Then("^operation is successful$")
    public void status() {
        assertEquals(200, IntegrationTest.getTestContext().getStatus());
    }

    @Then("^operation should be finished with (\\w+) \"(.*)\" error$")
    public void raiseError(Integer status, String error) {
        assertEquals(status, IntegrationTest.getTestContext().getResponse().getStatusCode().value());
        assertEquals(error, IntegrationTest.getTestContext().getResponse().getBody());
    }
}
