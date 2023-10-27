package com.sytoss.checktask.stp.bdd.then;

import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import io.cucumber.java.en.Then;
import org.apache.commons.collections4.CollectionUtils;

import static org.junit.jupiter.api.Assertions.*;

public class ThenStepTest extends CheckTaskIntegrationTest {

    @Then("^request should be processed with error (.*)")
    public void requestShouldBeProcessedWithError(Integer code) {
        assertEquals(code, getTestExecutionContext().getResponse().getStatusCode().value());
    }

    @Then("request should be processed successfully")
    public void requestShouldBeProcessedSuccessfully() {
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
    public void shouldReturnQueryIsValid(QueryResult queryResult) {
        QueryResult response = (QueryResult) getTestExecutionContext().getResponse().getBody();
        assertTrue(CollectionUtils.isEqualCollection(queryResult.getHeader(), response.getHeader()), "Headers is equals");
        for (int i = 0; i < queryResult.getResultMapList().size(); i++) {
            for (String columnName: queryResult.getHeader()) {
                Object columnValue = queryResult.getValue(i,columnName);
                assertEquals(columnValue.toString(), response.getValue(i,columnName.toUpperCase()).toString());
            }
        }
    }

    @Then("^operation should be finished with \"(.*)\" error$")
    public void operationShouldBeFinishedWithError(int statusCode) {
        assertEquals(statusCode, getTestExecutionContext().getResponse().getStatusCode().value());
    }
}
