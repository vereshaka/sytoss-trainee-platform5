package com.sytoss.checktask.stp.cucumber;

import bom.QueryResult;
import com.sytoss.checktask.stp.CucumberIntegrationTest;
import io.cucumber.java.en.When;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;


public class WhenStepTest extends CucumberIntegrationTest {

    @When("student's answer is checking with {string}, {string}")
    public void studentsAnswerIsCheckingWith(String answer, String etalon) throws SQLException {
        HashMap<String, Object> hashMapAnswer = new HashMap<>();
        hashMapAnswer.put("answer", "it`s answer");
        HashMap<String, Object> hashMapEtalon = new HashMap<>();
        hashMapEtalon.put("etalon", "it`s etalon");
        when(databaseHelperService.get().getExecuteQueryResult(answer)).thenReturn(new QueryResult(List.of(hashMapAnswer)));
        when(databaseHelperService.get().getExecuteQueryResult(etalon)).thenReturn(new QueryResult(List.of(hashMapEtalon)));
        databaseHelperService.get().getExecuteQueryResult(answer);
        databaseHelperService.get().getExecuteQueryResult(etalon);
    }
}
