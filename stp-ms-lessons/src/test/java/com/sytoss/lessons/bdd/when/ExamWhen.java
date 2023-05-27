package com.sytoss.lessons.bdd.when;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import io.cucumber.java.en.When;

public class ExamWhen extends CucumberIntegrationTest {

    @When("a request to save the exam calls")
    public void saveExamRequest() {

    }

    @When("exam has topics in discipline {word}")
    public void examHasTopicsInDiscipline(String disciplineName) {

    }
}
