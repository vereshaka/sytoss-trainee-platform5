package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

public class TaskGiven extends CucumberIntegrationTest {

    @Given("tasks exist")
    public void tasksExist(DataTable tasks) {
        getTaskConnector().deleteAll();
        getTopicConnector().deleteAll();
        getDisciplineConnector().deleteAll();
        List<Map<String, String>> rows = tasks.asMaps();
        getListOfTasksFromDataTable(rows);
    }

    private void getListOfTasksFromDataTable(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            String disciplineName = columns.get("discipline");
            Long teacherId = TestExecutionContext.getTestContext().getTeacherId();
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, teacherId);
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setName(disciplineName);
                disciplineDTO.setTeacher(getTeacherConnector().getReferenceById(teacherId));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }

            String topicName = columns.get("topic");
            TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
            if (topicDTO == null) {
                topicDTO = new TopicDTO();
                topicDTO.setName(topicName);
                topicDTO.setDiscipline(disciplineDTO);
                topicDTO = getTopicConnector().save(topicDTO);
                if (TestExecutionContext.getTestContext().getTopicId() == null) {
                    TestExecutionContext.getTestContext().setTopicId(topicDTO.getId());
                }
            }

            String taskQuestion = columns.get("task");
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setQuestion(taskQuestion);
            taskDTO.setTaskDomainDTO(getTaskDomainConnector().getByName("TaskDomain"));
            taskDTO.setTopics(List.of(topicDTO));
            getTaskConnector().save(taskDTO);
        }
    }
}
