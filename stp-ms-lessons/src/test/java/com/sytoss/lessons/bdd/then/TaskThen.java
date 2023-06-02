package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskThen extends CucumberIntegrationTest {

    @Then("^should return task with \"(.*)\" question$")
    public void taskShouldBeReturned(String question) {
        Task task = (Task) TestExecutionContext.getTestContext().getResponse().getBody();
        assert task != null;
        assertEquals(question, task.getQuestion());
    }
}
