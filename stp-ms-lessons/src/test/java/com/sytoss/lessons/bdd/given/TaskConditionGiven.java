package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskConditionDTO;
import com.sytoss.lessons.dto.TaskDTO;
import io.cucumber.java.en.Given;

public class TaskConditionGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" condition with (.*) type does not exist in this task$")
    public void  conditionExists(String conditionName, ConditionType type) {
        TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByNameAndTypeAndTaskId(conditionName, type, TestExecutionContext.getTestContext().getTaskId());
        if (taskConditionDTO != null)
        {
            TaskDTO taskDTO = getTaskConnector().getReferenceById(TestExecutionContext.getTestContext().getTaskId());
            taskDTO.getConditions().remove(taskConditionDTO);
            getTaskConnector().save(taskDTO);
        }
    }
}