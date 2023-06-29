package com.sytoss.checktask.stp.bdd.given;

import com.sytoss.checktask.stp.bdd.CucumberIntegrationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import io.cucumber.java.en.Given;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GivenStepTest extends CucumberIntegrationTest {

    @Given("Request contains database script as in {string}")
    public void givenDatabaseScript(String script) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("scripts/" + script).getFile());
        List<String> data = Files.readAllLines(Path.of(file.getPath()));
        TestContext.getInstance().getCheckTaskParameters().setScript(String.join("\n", data));
    }

    @Given("etalon SQL is {string}")
    public void givenEtalonScript(String answer) {
        TestContext.getInstance().getCheckTaskParameters().setEtalon(answer);
    }

    @Given("^answer should contains \"(.*)\" condition with \"(.*)\" type$")
    public void givenCondition(String condition, String conditionType) {
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setValue(condition);
        taskCondition.setType(ConditionType.valueOf(conditionType));
        TestContext.getInstance().getCheckTaskParameters().getConditions().add(taskCondition);
    }

    @Given("check SQL is {string}")
    public void givenAnswerScript(String answer) {
        TestContext.getInstance().getCheckTaskParameters().setRequest(answer);
    }
}
