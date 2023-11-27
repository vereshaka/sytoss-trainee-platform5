package com.sytoss.producer.bdd.then;

import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.*;

public class CommonThen extends TestProducerIntegrationTest {

    @Then("^operation is successful$")
    public void status() {
        assertEquals(200, getTestExecutionContext().getDetails().getStatusCode());
    }

    @Then("^operation should be finished with (\\w+) \"(.*)\" error$")
    public void raiseError(Integer status, String error) {
        assertNotNull(getTestExecutionContext().getDetails().getResponse());
        assertEquals(status, getTestExecutionContext().getDetails().getResponse().getStatusCode().value());
        if ("<null>".equals(error)){
            assertNull(getTestExecutionContext().getDetails().getResponse().getBody());
        }else {
            assertEquals(error, getTestExecutionContext().getDetails().getResponse().getBody());
        }
    }
}
