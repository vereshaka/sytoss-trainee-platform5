package com.sytoss.lessons.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class TaskThen extends CucumberIntegrationTest {

    @Then("should return task with question {string}")
    public void shouldReturnTaskWithQuestion(String question) throws JsonProcessingException {
        List<Task> tasks = getMapper().readValue(TestExecutionContext.getTestContext().getResponse().getBody().toString(),
                new TypeReference<List<Task>>() {});
        Assertions.assertEquals(question, tasks.get(0).getQuestion());
    }
}
