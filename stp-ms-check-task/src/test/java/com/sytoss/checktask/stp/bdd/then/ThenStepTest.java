package com.sytoss.checktask.stp.bdd.then;

import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import io.cucumber.java.en.Then;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class ThenStepTest extends CheckTaskIntegrationTest {

    @Then("request should be processed successfully")
    public void requestShouldBeProcessedSuccessfully() {
        ResponseEntity response = getTestExecutionContext().getResponse();
        assertEquals(200, response.getStatusCode().value());
    }

    @Then("Grade value is {double}")
    public void gradeValueIs(double value) {
        ResponseEntity<Score> response = getTestExecutionContext().getResponse();
        assertEquals(value, response.getBody().getValue());
    }

    @Then("Grade message is {string}")
    public void gradeMessageIs(String message) {
        ResponseEntity<Score> response = getTestExecutionContext().getResponse();
        assertEquals(message, response.getBody().getComment());
    }

    @Then("^should return that etalon is valid$")
    public void shouldReturnEtalonIsValid() {
        ResponseEntity<IsCheckEtalon> response = getTestExecutionContext().getResponse();
        assertTrue(response.getBody().isChecked());
    }

    @Then("^should return that etalon is not valid$")
    public void shouldReturnEtalonIsNotValid() {
        ResponseEntity<IsCheckEtalon> response = getTestExecutionContext().getResponse();
        assertFalse(response.getBody().isChecked());
    }
}
