package com.sytoss.producer.bdd.then;

import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommonThen {

    @Then("^operation is successful$")
    public void status() {
        assertEquals(200, IntegrationTest.getTestContext().getStastuCode());
    }

    @Then("^operation should be finished with (\\w+) \"(.*)\" error$")
    public void raiseError(Integer status, String error) {
        assertNotNull(IntegrationTest.getTestContext().getResponse());
        assertEquals(status, IntegrationTest.getTestContext().getResponse().getStatusCode().value());
        assertEquals(error, IntegrationTest.getTestContext().getResponse().getBody());
    }
}
