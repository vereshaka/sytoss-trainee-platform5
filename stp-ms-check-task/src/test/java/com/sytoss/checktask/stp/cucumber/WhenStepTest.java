package com.sytoss.checktask.stp.cucumber;

import com.sytoss.checktask.stp.CucumberIntegrationTest;
import io.cucumber.java.en.When;


public class WhenStepTest extends CucumberIntegrationTest {

    @When("student's answer is checking with {string}, {string}")
    public void studentsAnswerIsCheckingWith(String answer, String etalon) {
        TestContext.getInstance().setAnswer(databaseHelperService.get().getExecuteQueryResult(answer));
        TestContext.getInstance().setEtalon(databaseHelperService.get().getExecuteQueryResult(etalon));
    }

    @When("database drop is initiated")
    public void databaseDropIsInitiated() {
        databaseHelperService.get().dropDatabase();
    }
}
