package com.sytoss.checktask.stp.cucumber;

import bom.QueryResult;
import com.sytoss.checktask.stp.CucumberIntegrationTest;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

public class ThenStepTest extends CucumberIntegrationTest {

    private final List<QueryResult> resultList = new ArrayList<>();

    @Then("{string} should be")
    public void answerShouldBe(String parameter, Map<String, Object> mapAnswer) {
        Assertions.assertEquals("it`s " + parameter, mapAnswer.get(parameter));
        resultList.add(new QueryResult(List.of(new HashMap<>(mapAnswer))));
    }

    @Then("answer and etalon should have same number of columns and rows")
    public void answerAndEtalonShouldBeGotFromDatabase() {
        if (!resultList.isEmpty()) {
            Assertions.assertEquals(resultList.get(0).getResultMapList().size(),
                    resultList.get(1).getResultMapList().size());
            Assertions.assertEquals(resultList.get(0).getResultMapList().get(0).size(),
                    resultList.get(1).getResultMapList().get(0).size());
        }
    }

    @Then("database should be dropped")
    public void databaseShouldBeDropped() {
        databaseHelperService.get().dropDatabase();
        verify(databaseHelperService.get()).dropDatabase();
    }
}
