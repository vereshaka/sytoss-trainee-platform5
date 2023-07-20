package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.enums.ConvertToPumlParameters;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bom.TaskDomainModel;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.stp.test.FileUtils;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TaskDomainWhen extends LessonsIntegrationTest {

    @When("^system create \"(.*)\" task domain$")
    public void requestSentCreateTaskDomain(String name) {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(name);
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/task-domain";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
        ResponseEntity<TaskDomain> responseEntity = doPost(url, httpEntity, TaskDomain.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^system create \"(.*)\" task domain when it exist$")
    public void requestSentCreateTaskDomainWhenItExist(String name) {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(name);
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/task-domain";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^system retrieve information about \"(.*)\" task domain$")
    public void systemTryToFindTaskDomainById(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, getTestExecutionContext().getDetails().getDisciplineId());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        if (taskDomainDTO == null) {
            String url = "/api/task-domain/123";
            ResponseEntity<String> responseEntity = doGet(url, requestEntity, String.class);
            getTestExecutionContext().setResponse(responseEntity);
        }
        if (taskDomainDTO != null) {
            String url = "/api/task-domain/" + taskDomainDTO.getId();
            ResponseEntity<TaskDomain> responseEntity = doGet(url, requestEntity, TaskDomain.class);
            getTestExecutionContext().setResponse(responseEntity);
        }
    }

    @When("^receive all task domains by \"(.*)\" discipline$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        String url = "/api/discipline/" + discipline.getId() + "/task-domains";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<TaskDomain>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher updates \"(.*)\" task domain to \"(.*)\"$")
    public void teacherUpdatesTaskDomainTo(String oldTaskDomainName, String newTaskDomainName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(oldTaskDomainName, disciplineDTO.getId());
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(newTaskDomainName);
        if (taskDomainDTO != null) {
            taskDomain.setDatabaseScript(taskDomainDTO.getDatabaseScript());
            taskDomain.setDataScript(taskDomainDTO.getDataScript());
        }
        Discipline discipline = new Discipline();
        discipline.setId(disciplineDTO.getId());
        Teacher teacher = new Teacher();
        teacher.setId(getTestExecutionContext().getDetails().getTeacherId());
        discipline.setTeacher(teacher);
        taskDomain.setDiscipline(discipline);
        if (!getTestExecutionContext().getDetails().getPersonalExams().isEmpty()) {
            String url = "/api/task-domain/" + taskDomainDTO.getId();
            HttpHeaders httpHeaders = getDefaultHttpHeaders();
            HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
            when(getPersonalExamConnector().taskDomainIsUsed(anyLong())).thenReturn(true);
            ResponseEntity<String> responseEntity = doPut(url, httpEntity, String.class);
            getTestExecutionContext().setResponse(responseEntity);
        } else if (taskDomainDTO == null) {
            String url = "/api/task-domain/123";
            HttpHeaders httpHeaders = getDefaultHttpHeaders();
            HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
            ResponseEntity<String> responseEntity = doPut(url, httpEntity, String.class);
            getTestExecutionContext().setResponse(responseEntity);
        } else {
            String url = "/api/task-domain/" + taskDomainDTO.getId();
            HttpHeaders httpHeaders = getDefaultHttpHeaders();
            HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
            ResponseEntity<TaskDomain> responseEntity = doPut(url, httpEntity, TaskDomain.class);
            getTestExecutionContext().setResponse(responseEntity);
        }
    }

    @When("^system generate image of scheme and save in \"(.*)\" task domain$")
    public void requestSentCreateImageForTaskDomain(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, getTestExecutionContext().getDetails().getDisciplineId());
        String url = "/api/task-domain/puml/ALL";
        String pumlScript;
        pumlScript = FileUtils.readFromFile("puml/script.puml");
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(pumlScript, httpHeaders);
        ResponseEntity<byte[]> responseEntity = doPut(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^system generate image of scheme and save in \"(.*)\" task domain with empty script$")
    public void requestSentCreateImageForTaskDomainWithWrongScript(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, getTestExecutionContext().getDetails().getDisciplineId());
        String url = "/api/task-domain/puml/"+ ConvertToPumlParameters.ALL;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = doPut(url, httpEntity, String.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^system retrieve information about \"(.*)\" task domain tasks count")
    public void systemRetrieveInformationAboutTaskDomainTasksCount(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, getTestExecutionContext().getDetails().getDisciplineId());
        String url = "/api/task-domain/" + taskDomainDTO.getId() + "/overview";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<TaskDomainModel> responseEntity = doGet(url, httpEntity, TaskDomainModel.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^system retrieve information about (.*) task domain tasks$")
    public void systemRetrieveInformationAboutTaskDomainTasks(String taskDomainStringId) {
        Long taskDomainId = getTestExecutionContext().getIdMapping().get(taskDomainStringId);
        String url = "/api/task-domain/" + taskDomainId + "/tasks";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Task>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }
}
