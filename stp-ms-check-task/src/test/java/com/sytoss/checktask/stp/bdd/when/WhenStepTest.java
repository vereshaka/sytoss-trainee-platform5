package com.sytoss.checktask.stp.bdd.when;

import com.sytoss.checktask.stp.bdd.CucumberIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import io.cucumber.java.en.When;

import java.sql.SQLException;


public class WhenStepTest extends CucumberIntegrationTest {

    @When("student's answer is checking with {string}, {string}")
    public void studentsAnswerIsCheckingWith(String answer, String etalon) throws SQLException {
        TestContext.getInstance().setAnswer(TestContext.getInstance().getDatabaseHelperService().get().getExecuteQueryResult(answer));
        TestContext.getInstance().setEtalon(TestContext.getInstance().getDatabaseHelperService().get().getExecuteQueryResult(etalon));
    }

    @When("database drop is initiated")
    public void databaseDropIsInitiated() {
        TestContext.getInstance().getDatabaseHelperService().get().dropDatabase();
    }
}
