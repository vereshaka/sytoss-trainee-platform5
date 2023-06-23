package com.sytoss.checktask.stp.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.checktask.stp.bdd.CucumberIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.personalexam.Grade;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class ThenStepTest extends CucumberIntegrationTest {

    @Then("request should be processed successfully")
    public void requestShouldBeProcessedSuccessfully() throws JsonProcessingException {
        Assertions.assertEquals(200, TestContext.getInstance().getResponseEntity().getStatusCode().value());
        Grade grade = getMapper().readValue(TestContext.getInstance().getResponseEntity().getBody(), Grade.class);
        TestContext.getInstance().setGrade(grade);
    }

    @Then("Grade value is {double}")
    public void gradeValueIs(double value) {
        Assertions.assertEquals(value, TestContext.getInstance().getGrade().getValue());
    }

    @Then("Grade message is {string}")
    public void gradeMessageIs(String message) {
        Assertions.assertEquals(message, TestContext.getInstance().getGrade().getComment());
    }
}
