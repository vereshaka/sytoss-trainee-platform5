package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
public class TaskGiven extends LessonsIntegrationTest {

    @Given("^task with question \"(.*)\" exists$")
    public void taskExists(String question) {
        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(question, getTestExecutionContext().getDetails().getDisciplineId());

        if (taskDTO == null) {
            TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getReferenceById(getTestExecutionContext().getDetails().getTaskDomainId());
            TopicDTO topicDTO = getTopicConnector().getReferenceById(getTestExecutionContext().getDetails().getTopicId());
            taskDTO = new TaskDTO();
            taskDTO.setQuestion(question);
            taskDTO.setTopics(List.of(topicDTO));
            taskDTO.setEtalonAnswer("Etalon answer");
            taskDTO.setTaskDomain(taskDomainDTO);
            taskDTO = getTaskConnector().save(taskDTO);
        }
        getTestExecutionContext().getDetails().setTaskId(taskDTO.getId());
    }

    @Given("^task with question \"(.*)\" doesnt exist$")
    public void taskNotExist(String question) {
        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(question, getTestExecutionContext().getDetails().getDisciplineId());
        if (taskDTO != null) {
            getTaskConnector().delete(taskDTO);
        }
    }

    @Given("tasks exist")
    public void tasksExist(DataTable tasks) {
        List<Map<String, String>> rows = tasks.asMaps();
        getListOfTasksFromDataTable(rows);
    }

    private void getListOfTasksFromDataTable(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());
            TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(columns.get("task"), disciplineDTO.getId());
            if (taskDTO == null) {
                taskDTO = new TaskDTO();
                taskDTO.setQuestion(columns.get("task"));
                String conditionValue = columns.get("condition");
                String type = columns.get("type");
                if (conditionValue != null) {
                    TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByValueAndType(conditionValue, ConditionType.valueOf(type));
                    if (taskConditionDTO == null) {
                        taskConditionDTO = new TaskConditionDTO();
                        taskConditionDTO.setValue(conditionValue);
                        taskConditionDTO.setType(ConditionType.CONTAINS);
                        taskConditionDTO = getTaskConditionConnector().save(taskConditionDTO);
                        taskDTO.getConditions().add(taskConditionDTO);
                    } else {
                        taskDTO.getConditions().add(taskConditionDTO);
                    }
                }
                taskDTO.setTaskDomain(getTaskDomainConnector().getReferenceById(getTestExecutionContext().getDetails().getTaskDomainId()));
                List<TopicDTO> topicDTOList = new ArrayList<>();
                topicDTOList.add(getTopicConnector().getReferenceById(getTestExecutionContext().getDetails().getTopicId()));
                taskDTO.setTopics(topicDTOList);
                taskDTO = getTaskConnector().save(taskDTO);
            } else {
                String conditionName = columns.get("condition");
                String type = columns.get("type");
                if (conditionName != null) {
                    TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByValueAndType(conditionName, ConditionType.valueOf(type));
                    if (taskConditionDTO == null) {
                        taskConditionDTO = new TaskConditionDTO();
                        taskConditionDTO.setValue(conditionName);
                        taskConditionDTO.setType(ConditionType.CONTAINS);
                        taskConditionDTO = getTaskConditionConnector().save(taskConditionDTO);
                    }
                    if (!taskDTO.getConditions().contains(taskConditionDTO)) {
                        taskDTO.getConditions().add(taskConditionDTO);
                    }
                    taskDTO = getTaskConnector().save(taskDTO);
                }
            }
            getTestExecutionContext().getDetails().setTaskId(taskDTO.getId());
        }
    }

    @Given("^task with id (.*) doesnt exist")
    public void taskWithIdDoesntExist(String taskId) {
        if (getTestExecutionContext().getIdMapping().get(taskId) != null) {
            TaskDTO taskDto = getTaskConnector().getById(getTestExecutionContext().getIdMapping().get(taskId));
            getTaskConnector().delete(taskDto);
        } else {
            TaskDTO taskDto = getTaskConnector().getById(12345L);
            if (taskDto != null) {
                getTaskConnector().delete(taskDto);
            }
            getTestExecutionContext().registerId(taskId, 12345L);
        }
    }

    @And("^task with question \"(.*)\" exists for this task domain")
    public void taskWithQuestionExistsForThisTaskDomain(String question) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setQuestion(question);
        TaskDomainDTO taskDomain = getTaskDomainConnector().getReferenceById(getTestExecutionContext().getDetails().getTaskDomainId());
        taskDTO.setTaskDomain(taskDomain);
        getTaskConnector().save(taskDTO);
    }

    @Given("^Request contains database script as in \"(.*)\"$")
    public void givenDatabaseScript(String script) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("script/" + script).getFile());
        List<String> data = Files.readAllLines(Path.of(file.getPath()));
        getTestExecutionContext().getDetails().getCheckRequestParameters().setScript(String.join("\n", data));
    }

    @Given("^request is \"(.*)\"$")
    public void givenAnswerScript(String request) {
        getTestExecutionContext().getDetails().getCheckRequestParameters().setRequest(request);
    }

    @Given("^task domain tasks exist")
    public void taskDomainTasksExist(List<TaskDTO> tasks) {
        for (TaskDTO task : tasks) {
            TaskDTO taskDTO = getTaskConnector().getByQuestionAndTaskDomainId(task.getQuestion(), task.getTaskDomain().getId());
            if (taskDTO == null) {
                getTaskConnector().save(task);
            }
        }
    }
}