package com.sytoss.checktask.stp.bdd.given;

import com.sytoss.checktask.stp.bdd.CheckTaskIntegrationTest;
import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import io.cucumber.java.en.Given;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GivenStepTest extends CheckTaskIntegrationTest {

    @Given("^Request contains database script as in \"(.*)\"$")
    public void givenDatabaseScript(String script) {
        String data = null;
        try {
            data = IOUtils.toString(getClass().getResourceAsStream("/data/" + script), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getTestExecutionContext().getDetails().getCheckTaskParameters().setScript(data);
    }

    @Given("^Request contains database script from \"(.*)\" puml$")
    public void givenDatabaseScriptFromPuml(String script) {
        String data = null;
        try {
            data = new PumlConvertor().convertToLiquibase(IOUtils.toString(getClass().getResourceAsStream("/data/" + script), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    @Given("^check SQL is$")
    public void givenAnswerMultiLineScript(String answer) {
        getTestExecutionContext().getDetails().getCheckTaskParameters().setRequest(answer);
    }
}
