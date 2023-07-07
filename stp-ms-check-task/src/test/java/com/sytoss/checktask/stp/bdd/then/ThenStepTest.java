package com.sytoss.checktask.stp.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.checktask.model.QueryResult;
import com.sytoss.checktask.stp.bdd.CucumberIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.HashMap;
import java.util.List;

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

    @Then("^query result should be$")
    public void shouldReturnQueryIsValid(DataTable table) throws JsonProcessingException {
        QueryResult response = getMapper().readValue(TestContext.getInstance().getResponseEntity().getBody(), new TypeReference<>() {
        });
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
        assertEquals(statusCode, TestContext.getInstance().getResponseEntity().getStatusCode().value());
    }
}
