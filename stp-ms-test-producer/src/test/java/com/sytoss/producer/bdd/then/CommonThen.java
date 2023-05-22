package com.sytoss.producer.bdd.then;

import com.sytoss.producer.bdd.common.IntegrationTest;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonThen {

    @Then("^operation is successful$")
    public void status() {
        assertEquals(200, IntegrationTest.getTestContext().getResponse().getStatusCode().value());
    }
}
