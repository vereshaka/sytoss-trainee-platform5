package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void tasksShouldBeReceived(DataTable tasksDataTable) {
        List<Map<String, String>> rows = tasksDataTable.asMaps();
        List<Task> tasks = getListOfTasksFromDataTable(rows);
        List<Task> tasksFromResponse = (List<Task>) TestExecutionContext.getTestContext().getResponse().getBody();
        Assertions.assertEquals(tasks.size(), tasksFromResponse.size());
        Assertions.assertIterableEquals(tasks.stream().map(Task::getQuestion).toList(),
                tasksFromResponse.stream().map(Task::getQuestion).toList());
        List<List<Topic>> allTopicsDataTable = tasks.stream().map(Task::getTopics).toList();
        List<List<Topic>> allTopicsFromResponse = tasksFromResponse.stream().map(Task::getTopics).toList();
        Assertions.assertIterableEquals(allTopicsDataTable.stream().map(el2->el2.stream().map(Topic::getName).toList()).toList(),
                allTopicsFromResponse.stream().map(el2->el2.stream().map(Topic::getName).toList()).toList());

        List<List<Discipline>> allDisciplinesFromDataTable = allTopicsDataTable.stream().map(el2->el2.stream().map(Topic::getDiscipline).toList()).toList();
        List<List<Discipline>> allDisciplinesFromResponse =  allTopicsFromResponse.stream().map(el2->el2.stream().map(Topic::getDiscipline).toList()).toList();
        Assertions.assertIterableEquals(allDisciplinesFromDataTable.stream().map(el2->el2.stream().map(Discipline::getName).toList()).toList(),
                allDisciplinesFromResponse.stream().map(el2->el2.stream().map(Discipline::getName).toList()).toList());
    }

    private List<Task> getListOfTasksFromDataTable(List<Map<String, String>> rows) {
        List<TaskDTO> taskDTOS = new ArrayList<>();
        for (Map<String, String> columns : rows) {
            String disciplineName = columns.get("discipline");
            Long teacherId = TestExecutionContext.getTestContext().getTeacherId();
            DisciplineDTO disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacher(getTeacherConnector().getReferenceById(teacherId));

            String topicName = columns.get("topic");
            TopicDTO topicDTO = new TopicDTO();
            topicDTO.setName(topicName);
            topicDTO.setDiscipline(disciplineDTO);

            String taskQuestion = columns.get("task");
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setQuestion(taskQuestion);
            taskDTO.setTaskDomain(getTaskDomainConnector().getReferenceById(TestExecutionContext.getTestContext().getTaskDomainId()));
            taskDTO.setTopics(List.of(topicDTO));
            taskDTOS.add(taskDTO);
        }

        List<Task> tasks = new ArrayList<>();
        for (TaskDTO taskDTO : taskDTOS) {
            Task task = new Task();
            getTaskConvertor().fromDTO(taskDTO, task);
            tasks.add(task);
        }
        return tasks;
    }
}
