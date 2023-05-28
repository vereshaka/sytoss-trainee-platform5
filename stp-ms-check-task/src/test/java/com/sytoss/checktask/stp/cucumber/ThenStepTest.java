package com.sytoss.checktask.stp.cucumber;

import bom.QueryResult;
import com.sytoss.checktask.stp.CucumberIntegrationTest;
import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationError;
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
        Exception exception = Assertions.assertThrows(DatabaseCommunicationError.class,
                () -> databaseHelperService.get().getExecuteQueryResult("select * from answer"));
        Assertions.assertEquals(JdbcSQLSyntaxErrorException.class, exception.getCause().getClass());
        Assertions.assertEquals("Table \"ANSWER\" not found (this database is empty); SQL statement:\n" +
                "select * from answer [42104-214]", exception.getCause().getMessage());
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
