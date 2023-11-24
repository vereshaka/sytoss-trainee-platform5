package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.TaskConditionDTO;
import com.sytoss.lessons.dto.TaskDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class TaskConditionGiven extends LessonsIntegrationTest {

    @Given("^\"(.*)\" condition with (.*) type does not exist in this task$")
    public void conditionExists(String conditionValue, ConditionType type) {
        TaskDTO taskDTO = getTaskConnector().findById(getTestExecutionContext().getDetails().getTaskId()).orElse(null);
        TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByValueAndType(conditionValue, type);
        if (taskConditionDTO == null) {
            taskConditionDTO = new TaskConditionDTO();
            taskConditionDTO.setValue(conditionValue);
            taskConditionDTO.setType(type);
            taskConditionDTO = getTaskConditionConnector().save(taskConditionDTO);
        }
        if (!taskDTO.getConditions().isEmpty()) {
            taskDTO.setConditions(null);
            getTaskConnector().save(taskDTO);
        }
        getTestExecutionContext().getDetails().setTaskConditionId(taskConditionDTO.getId());
    }

    @And("^task with question \"(.*)\" has condition \"(.*)\" with type (.*)$")
    public void taskWithQuestionHasConditionWithType(String question, String condition, String type) {
        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTaskDomainId(question,getTestExecutionContext().getDetails().getTaskDomainId());
        TaskConditionDTO taskConditionDTO = new TaskConditionDTO();
        taskConditionDTO.setType(type.equals("CONTAINS") ? ConditionType.CONTAINS : ConditionType.NOT_CONTAINS);
        taskConditionDTO.setValue(condition);
        taskDTO.getConditions().add(taskConditionDTO);
        getTaskConnector().save(taskDTO);
    }
}