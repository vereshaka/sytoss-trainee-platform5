package com.sytoss.producer.bdd.then;

import com.sytoss.producer.bdd.TestProducerIntegrationTest;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class RatingsThen extends TestProducerIntegrationTest {
    @Then("^average estimation is (.*)$")
    public void questionShouldBe(Double grade) {
        Double responseGrade = getTestExecutionContext().getDetails().getRatingsResponse().getBody();
        Assertions.assertEquals(grade, responseGrade);
    }
}
