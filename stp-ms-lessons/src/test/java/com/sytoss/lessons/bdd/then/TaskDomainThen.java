package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bom.TaskDomainModel;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.stp.test.FileUtils;
import com.sytoss.stp.test.common.DataTableCommon;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskDomainThen extends LessonsIntegrationTest {

    @Then("^\"(.*)\" task domain should exists$")
    public void groupsShouldBeReceived(String name) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(name, getTestExecutionContext().getDetails().getDisciplineId());
        TaskDomain taskDomain = (TaskDomain) getTestExecutionContext().getResponse().getBody();
        assertNotNull(taskDomainDTO);
        assertEquals(taskDomain.getId(), taskDomainDTO.getId());
        assertEquals(taskDomain.getName(), taskDomainDTO.getName());
    }

    @Then("^system should been get \"(.*)\" information$")
    public void systemShouldGetTaskDomain(String taskDomainName) {
        TaskDomain taskDomain = (TaskDomain) getTestExecutionContext().getResponse().getBody();
        Assertions.assertNotNull(taskDomain);
        Assertions.assertEquals(taskDomainName, taskDomain.getName());
    }

    @Then("^task domains should received$")
    public void groupsShouldBeReceived(List<TaskDomain> taskDomains) {
        List<TaskDomain> results = (List<TaskDomain>) getTestExecutionContext().getResponse().getBody();
        assertNotNull(results);
        assertEquals(taskDomains.size(), results.size());

        int quantityOftaskDomains = 0;

        for (TaskDomain result : results) {
            for (TaskDomain taskDomain : taskDomains)
                if (result.getName().equals(taskDomain.getName())) {
                    quantityOftaskDomains++;
                }
        }
        assertEquals(quantityOftaskDomains, results.size());
    }

    @Then("^\"(.*)\" task domain with a script from \"(.*)\" should be$")
    public void taskDomainWithScriptShouldBe(String taskDomainName, String path) {
        TaskDomain taskDomain = (TaskDomain) getTestExecutionContext().getResponse().getBody();
        assertNotNull(taskDomain);
        assertEquals(taskDomainName, taskDomain.getName());
        String scriptFromFile = FileUtils.readFromFile("liquibase/" + path);
        assertEquals(scriptFromFile, taskDomain.getDatabaseScript());
        assertEquals(scriptFromFile, taskDomain.getDataScript());
    }

    @Then("^\"(.*)\" should have image$")
    public void taskDomainShouldHaveImage(String taskDomainName) {
        assertNotNull(getTestExecutionContext().getResponse().getBody());
    }

    @Then("^task domain have (.*) tasks")
    public void taskDomainHaveTask(int countOfTasks) {
        TaskDomainModel taskDomainModel = (TaskDomainModel) getTestExecutionContext().getResponse().getBody();
        assertEquals(countOfTasks, taskDomainModel.getCountOfTasks());
    }

    @Then("^task domain have tasks$")
    public void taskDomainHaveTasks(DataTable table) {
        List<Task> tasks = (List<Task>) getTestExecutionContext().getResponse().getBody();

        List<Map<String, String>> rows = table.asMaps();

        List<Task> tasksFromTable = new ArrayList<>();
        DataTableCommon dataTableCommon = new DataTableCommon();
        for (Map<String, String> row : rows) {
            Task task = dataTableCommon.mapTasks(row);
            String id = row.get("taskDomainId");
            TaskDomain taskDomain = new TaskDomain();
            taskDomain.setId(getTestExecutionContext().getIdMapping().get(id));
            task.setTaskDomain(taskDomain);
            tasksFromTable.add(task);
        }

        int quantityOfTasks = 0;
        assertEquals(tasksFromTable.size(), tasks.size());
        for (Task task : tasks) {
            for (Task taskFromTable : tasksFromTable)
                if (Objects.equals(taskFromTable.getQuestion(), task.getQuestion()) &&
                        task.getTaskDomain().getId().equals(taskFromTable.getTaskDomain().getId())) {
                    quantityOfTasks++;
                }
        }
        assertEquals(quantityOfTasks, tasks.size());
    }
}