package com.sytoss.lessons.bdd.then;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
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
    public void tasksShouldBeReceived(DataTable tasks) throws IOException {
        List<Map<String, String>> rows = tasks.asMaps();
        List<TaskDTO> taskDTOS = getListOfTasksFromDataTable(rows);
        List<TaskDTO> taskDTOsFromResponse = getMapper().readValue(TestExecutionContext.getTestContext().getResponse().getBody().toString(), new TypeReference<>() {
        });
        Assertions.assertEquals(taskDTOS.size(), taskDTOsFromResponse.size());
        if (taskDTOS.size() == taskDTOsFromResponse.size()) {
            for (int i = 0; i < taskDTOS.size(); i++) {
                Assertions.assertEquals(taskDTOS.get(i).getQuestion(), taskDTOsFromResponse.get(i).getQuestion());
                if (taskDTOS.get(i).getTopics().size() == taskDTOsFromResponse.get(i).getTopics().size()) {
                    List<TopicDTO> taskDTOSTopics = taskDTOS.get(i).getTopics().stream().toList();
                    List<TopicDTO> taskDTOSFromResponseTopics = taskDTOsFromResponse.get(i).getTopics().stream().toList();
                    for (int j = 0; j < taskDTOSTopics.size(); j++) {
                        Assertions.assertEquals(taskDTOSTopics.get(j).getName(), taskDTOSFromResponseTopics.get(j).getName());
                        Assertions.assertEquals(taskDTOSTopics.get(j).getDiscipline().getName(), taskDTOSFromResponseTopics.get(j).getDiscipline().getName());
                    }
                }
            }
        }
    }

    private List<TaskDTO> getListOfTasksFromDataTable(List<Map<String, String>> rows) {
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
            taskDTO.setTaskDomain(getTaskDomainConnector().getByName("TaskDomain"));
            taskDTO.setTopics(List.of(topicDTO));
            taskDTOS.add(taskDTO);
        }
        return taskDTOS;
    }
}
