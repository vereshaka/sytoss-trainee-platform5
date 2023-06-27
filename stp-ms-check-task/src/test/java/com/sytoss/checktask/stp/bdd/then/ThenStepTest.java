package com.sytoss.checktask.stp.bdd.then;

import com.sytoss.checktask.model.QueryResult;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ThenStepTest extends StpIntegrationTest {

    @Then("request should be processed successfully")
    public void requestShouldBeProcessedSuccessfully() {
        ResponseEntity<Score> response = getTestExecutionContext().getResponse();
        assertEquals(200, response.getStatusCode().value());
    }

    @Then("Grade value is {double}")
    public void gradeValueIs(double value) {
        assertEquals(value, TestContext.getInstance().getScore().getValue());
    }

    @Then("Grade message is {string}")
    public void gradeMessageIs(String message) {
        assertEquals(message, TestContext.getInstance().getScore().getComment());
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

    @Then("^query result should be$")
    public void shouldReturnEtalonIsNotValid(DataTable table) {
        ResponseEntity<QueryResult> response = getTestExecutionContext().getResponse();
        List<Map<String, String>> rows = table.asMaps();
        for (Map<String, String> column : rows) {

        }

        assertFalse(response.getBody().getResultMapList().isEmpty());
    }
}
