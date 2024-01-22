package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.*;
import com.sytoss.lessons.services.TaskService;
import com.sytoss.stp.test.common.DataTableCommon;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
public class TaskGiven extends LessonsIntegrationTest {

    @Autowired
    private TaskService taskService;

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
            taskDTO.setMode("AND");
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

    @Given("^task with specific id (.*) exists")
    public void taskWithIdExists(Long taskId) {
        try {
            //taskService.deleteTask(taskId);
        } catch (TaskNotFoundException e) {
        }
        try {
            Connection connection = getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM TASK WHERE ID = " + taskId);
            statement.execute("INSERT INTO TASK (ID, TASK_DOMAIN_ID, QUESTION, ETALON_ANSWER, MODE) " +
                    "VALUES(" + taskId + ", " + getTestExecutionContext().getDetails().getTaskDomainId() +
                    ", 'Generic Question#" + taskId + "', 'Generic Answer', 'AND')");
            while (true) {
                ResultSet rs = statement.executeQuery("select TASK_SEQ.nextVal from Dual");
                rs.next();
                int id = rs.getInt(1);
                if (id >= taskId + 5) {
                    break;
                }
            }
            statement.close();
            connection.commit();
            connection.close();
            connection = getDataSource().getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT ID FROM TASK where ID = " + taskId);
            assertTrue(rs.next());
            assertEquals(3L, rs.getLong(1));
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getEntityManager().clear();
        getTaskConnector().getReferenceById(taskId);
    }

    @Given("^task with id (.*) doesnt exist")
    public void taskWithIdDoesntExist(String taskId) {
        if (getTestExecutionContext().getIdMapping().get(taskId) != null) {
            TaskDTO taskDto = getTaskConnector().getById((Long) getTestExecutionContext().getIdMapping().get(taskId));
            getTaskConnector().delete(taskDto);
        } else {
            TaskDTO taskDto = getTaskConnector().getReferenceById(12345L);
            if (taskDto != null) {
                getTaskConnector().delete(taskDto);
            }
            getTestExecutionContext().registerId(taskId, 12345L);
        }
    }

    @Given("^task with question \"(.*)\" exists for this task domain")
    public void taskWithQuestionExistsForThisTaskDomain(String question) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setQuestion(question);
        taskDTO.setMode("AND");
        taskDTO.setEtalonAnswer("select * from dual");
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
            taskDomain.setId((Long) getTestExecutionContext().getIdMapping().get(id));
            task.setTaskDomain(taskDomain);
            task.setMode("AND");
            tasks.add(task);
        }

        for (Task task : tasks) {
            TaskDTO taskDTO = getTaskConnector().getByQuestionAndTaskDomainId(task.getQuestion(), task.getTaskDomain().getId());
            if (taskDTO == null) {
                taskDTO = new TaskDTO();
                taskDTO.setMode("AND");
                getTaskConvertor().toDTO(task, taskDTO);
                taskDTO.setTaskDomain(getTaskDomainConnector().getReferenceById(taskDTO.getTaskDomain().getId()));
                getTaskConnector().save(taskDTO);
            }
        }
    }

    @Given("^tasks for topic (.*) exist$")
    public void tasksForTopicExist(String topicId, DataTable dataTable) {
        List<Map<String, String>> taskRows = dataTable.asMaps();

        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(getTestExecutionContext().getDetails().getDisciplineId());

        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(11L);
        taskDomainDTO.setDiscipline(disciplineDTO);
        taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);

        TopicDTO topicDTO = getTopicConnector().getReferenceById(getTestExecutionContext().getDetails().getTopicId());

        for (Map<String, String> taskRow : taskRows) {
            TaskDTO task = new TaskDTO();
            task.setQuestion(taskRow.get("question"));
            task.setCoef(0.0);
            task.setTopics(List.of(topicDTO));
            task.setMode("AND");
            task.setTaskDomain(taskDomainDTO);
            task = getTaskConnector().save(task);
            getTestExecutionContext().registerId(taskRow.get("taskId"), task.getId().toString());
        }
    }
}
