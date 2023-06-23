package com.sytoss.checktask.stp.bdd.then;

import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.domain.bom.personalexam.Grade;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class ThenStepTest extends CheckTaskIntegrationTest {

    @Then("request should be processed successfully")
    public void requestShouldBeProcessedSuccessfully() {
        Assertions.assertEquals(200, getTestExecutionContext().getResponse().getStatusCode().value());
    }

    @Then("Grade value is {double}")
    public void gradeValueIs(double value) {
        Assertions.assertEquals(value, ((Grade) getTestExecutionContext().getResponse().getBody()).getValue());
    }

    @Then("Grade message is {string}")
    public void gradeMessageIs(String message) {
        Assertions.assertEquals(message, ((Grade) getTestExecutionContext().getResponse().getBody()).getComment());
    }
}
