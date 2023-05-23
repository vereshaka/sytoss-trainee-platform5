package com.sytoss.stp.cucumber;

import bom.QueryResult;
import com.sytoss.stp.CucumberIntegrationTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;

import java.sql.SQLException;

import static org.mockito.Mockito.when;

@Log4j2
public class CucumberTest extends CucumberIntegrationTest {

    @Given("database with script: {string}")
    public void givenDatabaseScript(String script) {
        databaseHelperService.generateDatabase(script);
    }

    @When("student's answer is checking with {string}, {string}")
    public void studentsAnswerIsCheckingWith(String answer, String etalon) throws SQLException {
        when(databaseHelperService.getQueryResult()).thenReturn(new QueryResult(answer, etalon));
        databaseHelperService.getExecuteQueryResult(answer, etalon);

    }

    @Then("answer and etalon should be got from database as QueryResult")
    public void answerAndEtalonShouldBeGotFromDatabase() {
        Assertions.assertEquals(QueryResult.class, databaseHelperService.getQueryResult().getClass());
    }

    @And("database should be dropped")
    public void databaseShouldBeDropped() throws Exception {
        databaseHelperService.dropDatabase();
    }
}
