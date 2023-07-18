package com.sytoss.checktask.stp.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ThenStepTest extends CheckTaskIntegrationTest {

    @Then("request should be processed successfully")
    public void requestShouldBeProcessedSuccessfully() throws JsonProcessingException {
        assertEquals(200, getTestExecutionContext().getResponse().getStatusCode().value());
    }

    @Then("^Grade value is (.*)$")
    public void gradeValueIs(double value) {
        Score score = (Score) getTestExecutionContext().getResponse().getBody();
        assertEquals(value, score.getValue());
    }

    @Then("^Grade message is \"(.*)\"$")
    public void gradeMessageIs(String message) {
        Score score = (Score) getTestExecutionContext().getResponse().getBody();
        assertEquals(message, score.getComment());
    }

    @Then("^should return that etalon is valid$")
    public void shouldReturnEtalonIsValid() {
        IsCheckEtalon isCheckEtalon = (IsCheckEtalon) getTestExecutionContext().getResponse().getBody();
        assertTrue(isCheckEtalon.isChecked());
    }

    @Then("^should return that etalon is not valid$")
    public void shouldReturnEtalonIsNotValid() {
        IsCheckEtalon isCheckEtalon = (IsCheckEtalon) getTestExecutionContext().getResponse().getBody();
        assertFalse(isCheckEtalon.isChecked());
    }

    @Then("^query result should be$")
    public void shouldReturnQueryIsValid(DataTable table) {
        QueryResult response = (QueryResult) getTestExecutionContext().getResponse().getBody();
        List<String> header = table.row(0);
        for (int i = 1; i < table.height(); i++) {
            HashMap<String, Object> resultRow = response.getRow(i - 1);
            for (int j = 0; j < header.size(); j++) {

                String columnName = header.get(j).toUpperCase();
                String columnValue = table.row(i).get(j);
                assertEquals(columnValue, resultRow.get(columnName).toString());
            }
        }
    }

    @Then("^operation should be finished with \"(.*)\" error$")
    public void operationShouldBeFinishedWithError(int statusCode) {
        assertEquals(statusCode, getTestExecutionContext().getResponse().getStatusCode().value());
    }
}
