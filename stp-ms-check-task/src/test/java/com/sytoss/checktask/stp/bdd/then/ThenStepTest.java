package com.sytoss.checktask.stp.bdd.then;

import bom.QueryResult;
import com.sytoss.checktask.stp.bdd.CucumberIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;
import org.junit.jupiter.api.Assertions;

public class ThenStepTest extends CucumberIntegrationTest {

    @Then("answer and etalon should have same number of columns and rows")
    public void answerAndEtalonShouldBeGotFromDatabase() {
        QueryResult queryResultAnswer = TestContext.getInstance().getAnswer();
        QueryResult queryResultEtalon = TestContext.getInstance().getEtalon();
        Assertions.assertEquals(queryResultAnswer.getResultMapList().size(),
                queryResultEtalon.getResultMapList().size());
        Assertions.assertEquals(queryResultAnswer.getResultMapList().get(0).size(),
                queryResultEtalon.getResultMapList().get(0).size());
    }

    @Then("database should be dropped")
    public void databaseShouldBeDropped() {
        Exception exception = Assertions.assertThrows(JdbcSQLSyntaxErrorException.class,
                () -> TestContext.getInstance().getDatabaseHelperService().get().getExecuteQueryResult("select * from answer"));
        Assertions.assertTrue(exception.getMessage().contains("Table \"ANSWER\" not found (this database is empty)"));
    }

    @Then("answer should be {string}")
    public void answerShouldBe(String answer) {
        Assertions.assertEquals(answer, TestContext.getInstance().getAnswer().getRow(0).get("ANSWER"));
    }

    @And("etalon should be {string}")
    public void etalonShouldBe(String etalon) {
        Assertions.assertEquals(etalon, TestContext.getInstance().getEtalon().getRow(0).get("ETALON"));
    }
}
