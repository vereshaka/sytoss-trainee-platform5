package com.sytoss.checktask.stp.cucumber;

import com.sytoss.checktask.stp.CucumberIntegrationTest;
import com.sytoss.domain.bom.QueryResult;
import io.cucumber.java.en.When;

import java.sql.SQLException;

import static org.mockito.Mockito.when;


public class WhenStepTest extends CucumberIntegrationTest {

    @When("student's answer is checking with {string}, {string}")
    public void studentsAnswerIsCheckingWith(String answer, String etalon) throws SQLException {
        when(databaseHelperService.get().getQueryResult()).thenReturn(new QueryResult(answer, etalon));
        databaseHelperService.get().getExecuteQueryResult(answer, etalon);
    }
}
