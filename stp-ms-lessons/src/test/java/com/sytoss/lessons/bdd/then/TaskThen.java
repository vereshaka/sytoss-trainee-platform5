package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.bson.assertions.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskThen extends LessonsIntegrationTest {

    @Then("^should return task with \"(.*)\" question$")
    public void taskShouldBeReturned(String question) {
        Task task = (Task) getTestExecutionContext().getResponse().getBody();
        assertNotNull(task);
        assertEquals(question, task.getQuestion());
    }

    @Then("^task with question \"(.*)\" should be created$")
    public void taskShouldBe(String question) {
        Task task = (Task) getTestExecutionContext().getResponse().getBody();
        assertNotNull(task);
        assertEquals(question, task.getQuestion());
        getTestExecutionContext().getDetails().setTaskConditions(task.getTaskConditions());
    }

    @Then("^tasks of topic with id (.*) should be received$")
    public void tasksShouldBeReceived(String topicKey, DataTable table) {
        List<Map<String, String>> rows = table.asMaps();
        List<Task> taskList = (List<Task>) getTestExecutionContext().getResponse().getBody();
        assertEquals(rows.size(), taskList.size());
        for (Map<String, String> columns : rows) {
            List<Task> foundTasks = taskList.stream().filter(item ->
                    item.getQuestion().equals(columns.get("task")) &&
                            (item.getTopics().size() == 1 && item.getTopics().get(0).getName().equals(columns.get("topic")) &&
                                    item.getTopics().get(0).getDiscipline().getName().equals(columns.get("discipline"))
                                    && item.getCode().equals(columns.get("code")))).toList();
            if (foundTasks.size() == 0) {
                fail("Task with question " + columns.get("task") + " and topic " + columns.get("topic") + " and discipline " + columns.get("discipline") + " and code " + columns.get("code") + " not found");
            }
            TopicDTO topic = getTopicConnector().getReferenceById((Long) getTestExecutionContext().getIdMapping().get(topicKey));
            List<TaskDTO> taskDTOS = getTaskConnector().findByTopicsIdOrderByCode(topic.getId());
            for (TaskDTO taskDTO : taskDTOS) {
                getTaskConnector().delete(taskDTO);
            }
            taskList.remove(foundTasks.get(0));
        }
        assertEquals(0, taskList.size());
    }

    @Then("task should be received")
    public void taskShouldBeReceived(DataTable table) {
        List<Map<String, String>> rows = table.asMaps();
        Task task = (Task) getTestExecutionContext().getResponse().getBody();
        int count = 0;
        for (TaskCondition taskCondition : task.getTaskConditions()) {
            for (Map<String, String> columns : rows) {
                assertEquals(columns.get("task"), task.getQuestion());
                if (taskCondition.getValue().equals(columns.get("condition"))) {
                    count++;
                }
            }
        }
        assertEquals(rows.size(), count);
    }

    @Then("^task with question \"(.*)\" should be assign to \"(.*)\" topic$")
    public void taskTopicShouldBe(String question, String topicName) {
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, getTestExecutionContext().getDetails().getDisciplineId());
        List<Task> tasks = (ArrayList<Task>) getTestExecutionContext().getResponse().getBody();
        for (Task task : tasks) {
            assertNotNull(task.getTopics());
            assertEquals(question, task.getQuestion());
            assertEquals(topicDTO.getName(), task.getTopics().get(1).getName());
        }

    }

    @Then("^\"(.*)\" condition with (.*) type should be in task with question \"(.*)\"$")
    public void taskShouldHaveConditionWithType(String conditionValue, ConditionType type, String question) {
        TaskDTO taskDTO = getTaskConnector().findById(getTestExecutionContext().getDetails().getTaskId()).orElse(null);
        assertEquals(conditionValue, taskDTO.getConditions().get(0).getValue());
        assertEquals(type, taskDTO.getConditions().get(0).getType());
        assertEquals(question, taskDTO.getQuestion());
    }

    @Then("^query result should be$")
    public void shouldReturnQueryIsValid(QueryResult queryResult) {
        QueryResult response = (QueryResult) getTestExecutionContext().getResponse().getBody();
        for (int i = 1; i < queryResult.getResultMapList().size(); i++) {
            for (String columnName : queryResult.getHeader()) {
                Object columnValue = queryResult.getValue(i, columnName);
                assertEquals(columnValue, response.getValue(i, columnName));
            }
        }
    }

    @Then("task should be deleted")
    public void taskShouldBeDeleted() {

    }

    @Then("task conditions should be")
    public void taskConditionsShouldBe(DataTable conditionsDataTable) {
        List<TaskCondition> taskConditionsFromResponse = getTestExecutionContext().getDetails().getTaskConditions();
        List<Map<String, String>> conditionsMaps = conditionsDataTable.asMaps();
        List<TaskCondition> taskConditions = new ArrayList<>();
        for (Map<String, String> conditionMap : conditionsMaps) {
            TaskCondition taskCondition = new TaskCondition();
            taskCondition.setValue(conditionMap.get("value"));
            taskCondition.setType(ConditionType.valueOf(conditionMap.get("type")));
            taskConditions.add(taskCondition);
        }

        for (TaskCondition taskCondition : taskConditions) {
            for (TaskCondition taskConditionFromResponse : taskConditionsFromResponse) {
                assertEquals(taskCondition.getValue(), taskConditionFromResponse.getValue());
                assertEquals(taskCondition.getType(), taskConditionFromResponse.getType());
                taskConditionsFromResponse.remove(taskConditionFromResponse);
                break;
            }
        }
    }

    @Then("^required command should be \"(.*)\"$")
    public void requiredCommandShouldBe(String requiredCommandEtalon) {
        String requiredCommand = ((Task) getTestExecutionContext().getResponse().getBody()).getRequiredCommand();
        assertEquals(requiredCommandEtalon, requiredCommand);
    }

    @Then("tasks with code exist")
    public void tasksWithCodeExist(DataTable dataTable) {
        List<Map<String, String>> tasks = dataTable.asMaps();
        for(Map<String, String> task : tasks){
            TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(task.get("taskDomain"),getTestExecutionContext().getDetails().getDisciplineId());
            assertNotNull(taskDomainDTO);
            TaskDTO taskDTO = getTaskConnector().getByCodeAndTaskDomainId(task.get("code"), taskDomainDTO.getId());
            assertNotNull(taskDTO);
        }
    }
}
