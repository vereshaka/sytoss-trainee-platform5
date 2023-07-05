package com.sytoss.checktask.stp.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.checktask.stp.bdd.CucumberIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.*;

public class ThenStepTest extends CucumberIntegrationTest {

    @Then("request should be processed successfully")
    public void requestShouldBeProcessedSuccessfully() throws JsonProcessingException {
        assertEquals(200, TestContext.getInstance().getResponseEntity().getStatusCode().value());
        Score score = getMapper().readValue(TestContext.getInstance().getResponseEntity().getBody(), Score.class);
        TestContext.getInstance().setScore(score);
    }

    @Then("^Grade value is (.*)$")
    public void gradeValueIs(double value) throws JsonProcessingException {
        assertEquals(value, TestContext.getInstance().getScore().getValue());
    }

    @Then("^Grade message is \"(.*)\"$")
    public void gradeMessageIs(String message) {
        assertEquals(message, TestContext.getInstance().getScore().getComment());
    }

    @Then("^should return that etalon is valid$")
    public void shouldReturnEtalonIsValid() throws JsonProcessingException {
        IsCheckEtalon isCheckEtalon = getMapper().readValue(TestContext.getInstance().getResponseEntity().getBody(), new TypeReference<>() {
        });
        assertTrue(isCheckEtalon.isChecked());
    }

    @Then("^should return that etalon is not valid$")
    public void shouldReturnEtalonIsNotValid() throws JsonProcessingException {
        IsCheckEtalon isCheckEtalon = getMapper().readValue(TestContext.getInstance().getResponseEntity().getBody(), new TypeReference<>() {
        });
        assertFalse(isCheckEtalon.isChecked());
    }
}
