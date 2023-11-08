package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bdd.common.TaskView;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.stp.test.FileUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
public class TaskDomainGiven extends LessonsIntegrationTest {

    @Given("^\"(.*)\" task domain exists$")
    public void taskDomainExist(String taskDomainName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, disciplineDTO.getId());
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
            taskDomainDTO.setName(taskDomainName);
            taskDomainDTO.setDatabaseScript("Test script");
            String databaseScript = FileUtils.readFromFile("puml/database.puml");
            taskDomainDTO.setDatabaseScript(databaseScript);
            String dataScript = FileUtils.readFromFile("puml/data.puml");
            taskDomainDTO.setDataScript(dataScript);
            taskDomainDTO.setDiscipline(disciplineDTO);
            taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);
        }
        getTestExecutionContext().getDetails().setTaskDomainId(taskDomainDTO.getId());
    }

    @Given("^\"(.*)\" task domain doesnt exist$")
    public void taskDomainNotExist(String name) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(name, getTestExecutionContext().getDetails().getDisciplineId());
        if (taskDomainDTO != null) {
            getTaskDomainConnector().delete(taskDomainDTO);
        }
    }

    @Given("^task domains exist$")
    public void taskDomainExist(List<TaskDomain> taskDomains) {
        List<TaskDomainDTO> taskDomainDTOS = new ArrayList<>();
        Teacher teacher = new Teacher();
        teacher.setId(getTestExecutionContext().getDetails().getTeacherId());
        for (TaskDomain taskDomain : taskDomains) {
            TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
            taskDomain.getDiscipline().setTeacher(teacher);
            getTaskDomainConvertor().toDTO(taskDomain, taskDomainDTO);
            taskDomainDTOS.add(taskDomainDTO);
        }
        for (TaskDomainDTO taskDomainDTO : taskDomainDTOS) {
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(taskDomainDTO.getDiscipline().getName(), getTestExecutionContext().getDetails().getTeacherId());
            if (disciplineDTO == null) {
                disciplineDTO = taskDomainDTO.getDiscipline();
                disciplineDTO.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }
            TaskDomainDTO result = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainDTO.getName(), disciplineDTO.getId());
            taskDomainDTO.setDiscipline(disciplineDTO);
            if (result == null) {
                String stringId = "";
                if (taskDomainDTO.getId() != null) {
                    stringId = "*" + taskDomainDTO.getId();
                    taskDomainDTO.setId(null);
                }
                TaskDomainDTO taskDomain = getTaskDomainConnector().save(taskDomainDTO);
                if (!stringId.equals("")) {
                    getTestExecutionContext().getIdMapping().put(stringId, taskDomain.getId());
                }
            }
        }
        getTestExecutionContext().getDetails().setTaskDomains(taskDomainDTOS);
    }

    @Given("^\"(.*)\" task domain with a script from \"(.*)\" exists for this discipline$")
    public void taskDomainForDiscipline(String taskDomainName, String path) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, getTestExecutionContext().getDetails().getDisciplineId());
        String scriptFromFile = null;
        try {
            scriptFromFile = IOUtils.toString(getClass().getResourceAsStream("/data/" + path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
        }
        taskDomainDTO.setName(taskDomainName);
        taskDomainDTO.setDiscipline(getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId()));
        taskDomainDTO.setDatabaseScript(scriptFromFile);
        taskDomainDTO.setDataScript(scriptFromFile);
        taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);

        getTestExecutionContext().getDetails().setTaskDomainId(taskDomainDTO.getId());
    }


    @Given("^\"(.*)\" task domain with \"(.*)\" db and \"(.*)\" data scripts exists for this discipline$")
    public void taskDomainForDiscipline(String taskDomainName, String dbScript, String dataScript, DataTable tasks) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());

        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, getTestExecutionContext().getDetails().getDisciplineId());
        String dbScriptFromFile = null;
        try {
            dbScriptFromFile = IOUtils.toString(getClass().getResourceAsStream("/data/" + dbScript), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String dataScriptFromFile = null;
        try {
            dataScriptFromFile = IOUtils.toString(getClass().getResourceAsStream("/data/" + dataScript), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (taskDomainDTO == null) {
            taskDomainDTO = new TaskDomainDTO();
        }
        taskDomainDTO.setName(taskDomainName);
        taskDomainDTO.setDiscipline(disciplineDTO);
        taskDomainDTO.setDatabaseScript(dbScriptFromFile);
        taskDomainDTO.setDataScript(dataScriptFromFile);
        taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);

        List<TaskView> tasksList= tasks.asMaps(String.class, String.class).stream().toList().stream().map(el -> new TaskView(el)).toList();

        for (TaskView item : tasksList){
            TaskDTO dto = new TaskDTO();
            dto.setTaskDomain(taskDomainDTO);
            dto.setQuestion(item.getQuestion());
            dto.setEtalonAnswer(item.getAnswer());
            dto.setCoef(1.0);
            dto.setTopics(new ArrayList<>());
            List<String> topicNames = Arrays.stream(item.getTopics().split(",")).map(el -> el.trim()).toList();
            for (String topicName: topicNames){
                TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
                dto.getTopics().add(topicDTO);
            }
            dto = getTaskConnector().save(dto);
            getTestExecutionContext().registerId(item.getId(), dto.getId());
        }
        getTestExecutionContext().getDetails().setTaskDomainId(taskDomainDTO.getId());
    }

    @Given("^\"(.*)\" task domain doesn't have image$")
    public void taskDomainShouldNotHaveImage(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, getTestExecutionContext().getDetails().getDisciplineId());
        if (taskDomainDTO.getImage() != null) {
            taskDomainDTO.setImage(null);
            getTaskDomainConnector().save(taskDomainDTO);
        }
    }

    @Given("^task domain with specific id (.*) and \"(.*)\" db, \"(.*)\" data scripts exists")
    public void taskDomainWithIdExists(Long taskDomainId, String dbScript, String dataScript) {
        try {
            String dbScriptFromFile = null;
            try {
                dbScriptFromFile = IOUtils.toString(getClass().getResourceAsStream("/data/" + dbScript), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String dataScriptFromFile = null;
            try {
                dataScriptFromFile = IOUtils.toString(getClass().getResourceAsStream("/data/" + dataScript), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Connection connection = getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM TASK_DOMAIN WHERE ID = " + taskDomainId);
            statement.execute("INSERT INTO TASK_DOMAIN (ID, NAME, DATABASE_SCRIPT, DATA_SCRIPT, DISCIPLINE_ID, SHORT_DESCRIPTION) VALUES(" + taskDomainId + ", 'task domain 2', 'db', 'data', " + getTestExecutionContext().getDetails().getDisciplineId() + ",'desc'"+")");
            while(true){
                ResultSet rs = statement.executeQuery("select TASK_DOMAIN_SEQ.nextVal from Dual");
                rs.next();
                int id = rs.getInt(1);
                if(id>=taskDomainId){
                    break;
                }
            }
            statement.close();
            connection.commit();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM TASK_DOMAIN where ID = " + taskDomainId);
            assertTrue(rs.next());
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getTestExecutionContext().getDetails().setTaskDomainId(taskDomainId);
        getEntityManager().clear();
    }

}
