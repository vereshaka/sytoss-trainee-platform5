package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskConditionDTO;
import com.sytoss.lessons.dto.TaskDTO;
import io.cucumber.java.en.Given;

public class TaskConditionGiven extends CucumberIntegrationTest {

    @Given("^\"(.*)\" condition with (.*) type does not exist in this task$")
    public void conditionExists(String conditionValue, ConditionType type) {
        TaskDTO taskDTO = getTaskConnector().getReferenceById(TestExecutionContext.getTestContext().getTaskId());
        //   TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByValueAndTypeAndTask(conditionValue, type, taskDTO);
        TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByValueAndType(conditionValue, type);
        if (taskConditionDTO == null) {
            taskConditionDTO = new TaskConditionDTO();
            taskConditionDTO.setValue(conditionValue);
            taskConditionDTO.setType(type);
            taskConditionDTO = getTaskConditionConnector().save(taskConditionDTO);
        } else {
            taskDTO.getConditions().remove(taskConditionDTO);
            getTaskConnector().save(taskDTO);
        }
        TestExecutionContext.getTestContext().setTaskConditionId(taskConditionDTO.getId());
    }
}