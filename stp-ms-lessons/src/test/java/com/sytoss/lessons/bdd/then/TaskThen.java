package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;

import static org.bson.assertions.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskThen extends CucumberIntegrationTest {

    @Then("^should return task with \"(.*)\" question$")
    public void taskShouldBeReturned(String question) {
        Task task = (Task) TestExecutionContext.getTestContext().getResponse().getBody();
        assertNotNull(task);
        assertEquals(question, task.getQuestion());
    }

    @Then("^task with question \"(.*)\" should be created$")
    public void taskShouldBe(String question) {
        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(question, TestExecutionContext.getTestContext().getDisciplineId());
        assertNotNull(taskDTO);
        assertEquals(question, taskDTO.getQuestion());
    }

    @Then("tasks should be received")
    public void tasksShouldBeReceived(DataTable table) {
        List<Map<String, String>> rows = table.asMaps();
        List<Task> taskList = (List<Task>) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(rows.size(), taskList.size());
        for (Map<String, String> columns : rows) {
            List<Task> foundTasks = taskList.stream().filter(item ->
                    item.getQuestion().equals(columns.get("task")) &&
                            item.getTopics().get(0).getName().equals(columns.get("topic")) &&
                            item.getTopics().get(0).getDiscipline().getName().equals(columns.get("discipline"))).toList();
            if (foundTasks.size() == 0) {
                fail("Task with question " + columns.get("task") + " and topic " + columns.get("topic") + " and discipline " + columns.get("discipline") + " not found");
            }
            taskList.remove(foundTasks.get(0));
        }
        assertEquals(0, taskList.size());
    }
}