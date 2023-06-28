package com.sytoss.checktask.stp.bdd.then;

import com.sytoss.checktask.model.QueryResult;
import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.Score;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Then;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ThenStepTest extends CheckTaskIntegrationTest {

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
    public void shouldReturnQueryIsValid(DataTable table) {
        QueryResult response = (QueryResult) getTestExecutionContext().getResponse().getBody();
        List<Map<String, String>> rows = table.asMaps();
        List<String> header = table.row(0);
        for (int i = 1; i<table.height(); i++) {
            HashMap<String, Object> resultRow = response.getRow(i - 1);
            for (int j = 0; j<header.size(); j++){

                String columnName = header.get(j);
                String columnValue = table.row(i).get(j);
               assertEquals(columnValue, resultRow.get(columnName).toString());
            }
        }
        /*for (Map<String, String> column : rows) {
            for (Map<String, Object> someMap : response.getResultMapList()) {
                for (String key : someMap.keySet()) {
                    Object someVar = someMap.get(key);
                    if (someVar instanceof Integer) {
                        assertTrue(someVar.equals(Integer.parseInt(column.get("id"))));
                    } else {
                        assertTrue(someVar.equals(column.get("name")));
                    }
                }
            }
        }*/
    }

/*    @DataTableType
    public QueryResult mapQueryResult(Map<String, String> row) {
        List<HashMap<String, Object>> resultList = new ArrayList<>();
        QueryResult queryResult = new QueryResult(resultList);
        HashMap<String, Object> query = new HashMap<>();
        query.put("id", Integer.parseInt(row.get("id")));
        query.put("name", row.get("name"));
        queryResult.getResultMapList().add(query);
        return queryResult;
    }*/
}
