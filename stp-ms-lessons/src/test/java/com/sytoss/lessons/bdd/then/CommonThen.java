package com.sytoss.lessons.bdd.then;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CommonThen extends CucumberIntegrationTest {

    @Then("^operation is successful$")
    public void status() {
        log.info("Body = [{}]", TestExecutionContext.getTestContext().getResponse().getBody());
        assertEquals(200, TestExecutionContext.getTestContext().getResponse().getStatusCode().value());
    }

    @Then("^operation should be finished with (\\w+) \"(.*)\" error$")
    public void raiseError(Integer status, String error) {
        assertEquals(status, TestExecutionContext.getTestContext().getResponse().getStatusCode().value());
        //assertEquals(error, TestExecutionContext.getTestContext().getResponse().getBody());
        // TODO: 05.07.2023 LarinI: Find a way to check an error message
    }
}
