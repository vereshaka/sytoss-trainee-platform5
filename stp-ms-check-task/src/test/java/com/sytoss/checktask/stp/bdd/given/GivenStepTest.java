package com.sytoss.checktask.stp.bdd.given;

import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.stp.test.FileUtils;
import io.cucumber.java.en.Given;
import io.micrometer.core.instrument.util.IOUtils;

import java.nio.charset.StandardCharsets;

public class GivenStepTest extends CheckTaskIntegrationTest {

    @Given("^Request contains database script as in \"(.*)\"$")
    public void givenDatabaseScript(String script) {
        String data = IOUtils.toString(getClass().getResourceAsStream("/data/" + script), StandardCharsets.UTF_8);
        getTestExecutionContext().getDetails().getCheckTaskParameters().setScript(data);
    }

    @Given("^etalon SQL is \"(.*)\"$")
    public void givenEtalonScript(String answer) {
        getTestExecutionContext().getDetails().getCheckTaskParameters().setEtalon(answer);
    }

    @Given("^answer should contains \"(.*)\" condition with \"(.*)\" type$")
    public void givenCondition(String condition, String conditionType) {
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setValue(condition);
        taskCondition.setType(ConditionType.valueOf(conditionType));
        getTestExecutionContext().getDetails().getCheckTaskParameters().getConditions().add(taskCondition);
    }

    @Given("^check SQL is \"(.*)\"$")
    public void givenAnswerScript(String answer) {
        getTestExecutionContext().getDetails().getCheckTaskParameters().setRequest(answer);
    }
}
