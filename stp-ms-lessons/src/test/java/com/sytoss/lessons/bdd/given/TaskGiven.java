package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.*;
import com.sytoss.stp.test.common.DataTableCommon;
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
import java.util.Objects;

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
            TaskDTO taskDto = getTaskConnector().getReferenceById(12345L);
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
    public void taskDomainTasksExist(DataTable table) {
        List<Map<String, String>> rows = table.asMaps();

        List<Task> tasks = new ArrayList<>();
        DataTableCommon dataTableCommon = new DataTableCommon();
        for (Map<String, String> row : rows) {
            Task task = dataTableCommon.mapTasks(row);
            String id = row.get("taskDomainId");
            TaskDomain taskDomain = new TaskDomain();
            taskDomain.setId(getTestExecutionContext().getIdMapping().get(id));
            task.setTaskDomain(taskDomain);
            tasks.add(task);
        }

        List<String> taskDomainIds = table.asMaps().stream().map(el -> el.get("taskDomainId")).toList();

        List<TaskDTO> taskDTOList = new ArrayList<>();
        for (Task task : tasks) {
            TaskDTO taskDTO = new TaskDTO();
            getTaskConvertor().toDTO(task, taskDTO);
            taskDTOList.add(taskDTO);
        }

        for (int i = 0; i < taskDTOList.size(); i++) {
            String taskDomainIdString = taskDomainIds.get(i);
            Long taskDomainId = getTestExecutionContext().getIdMapping().get(taskDomainIdString);
            TaskDomainDTO taskDomain = getTestExecutionContext().getDetails().getTaskDomains().stream().filter(el -> Objects.equals(el.getId(), taskDomainId)).toList().get(0);
            taskDTOList.get(i).setTaskDomain(taskDomain);
            TaskDTO taskDTO = getTaskConnector().getByQuestionAndTaskDomainId(taskDTOList.get(i).getQuestion(), taskDTOList.get(i).getTaskDomain().getId());
            if (taskDTO == null) {
                getTaskConnector().save(taskDTOList.get(i));
            }
        }
    }
}