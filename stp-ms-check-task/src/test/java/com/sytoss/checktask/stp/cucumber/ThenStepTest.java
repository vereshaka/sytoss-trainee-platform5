package com.sytoss.checktask.stp.cucumber;

import com.sytoss.checktask.stp.CucumberIntegrationTest;
import com.sytoss.domain.bom.QueryResult;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class ThenStepTest extends CucumberIntegrationTest {

    @Then("answer and etalon should be got from database as QueryResult")
    public void answerAndEtalonShouldBeGotFromDatabase() {
        Assertions.assertEquals(QueryResult.class, databaseHelperService.get().getQueryResult().getClass());
    }

    @And("database should be dropped")
    public void databaseShouldBeDropped() throws Exception {
        databaseHelperService.get().dropDatabase();
    }
}
