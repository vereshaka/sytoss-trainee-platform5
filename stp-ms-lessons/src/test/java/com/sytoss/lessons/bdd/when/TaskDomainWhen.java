package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.bom.TaskDomainModel;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TaskDomainWhen extends CucumberIntegrationTest {

    @When("^system create \"(.*)\" task domain$")
    public void requestSentCreateTaskDomain(String name) {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(name);
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/task-domain";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
        ResponseEntity<TaskDomain> responseEntity = doPost(url, httpEntity, TaskDomain.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system create \"(.*)\" task domain when it exist$")
    public void requestSentCreateTaskDomainWhenItExist(String name) {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(name);
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/task-domain";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system retrieve information about \"(.*)\" task domain$")
    public void systemTryToFindTaskDomainById(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, TestExecutionContext.getTestContext().getDisciplineId());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        if (taskDomainDTO == null) {
            String url = "/api/task-domain/123";
            ResponseEntity<String> responseEntity = doGet(url, requestEntity, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
        if (taskDomainDTO != null) {
            String url = "/api/task-domain/" + taskDomainDTO.getId();
            ResponseEntity<TaskDomain> responseEntity = doGet(url, requestEntity, TaskDomain.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }

    @When("^receive all task domains by \"(.*)\" discipline$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        String url = "/api/discipline/" + discipline.getId() + "/task-domains";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<TaskDomain>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher updates \"(.*)\" task domain to \"(.*)\"$")
    public void teacherUpdatesTaskDomainTo(String oldTaskDomainName, String newTaskDomainName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(TestExecutionContext.getTestContext().getDisciplineId());
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(oldTaskDomainName, disciplineDTO.getId());
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(newTaskDomainName);
        if (taskDomainDTO != null) {
            taskDomain.setScript(taskDomainDTO.getScript());
        }
        Discipline discipline = new Discipline();
        discipline.setId(disciplineDTO.getId());
        Teacher teacher = new Teacher();
        teacher.setId(TestExecutionContext.getTestContext().getTeacherId());
        discipline.setTeacher(teacher);
        taskDomain.setDiscipline(discipline);
        if (!TestExecutionContext.getTestContext().getPersonalExams().isEmpty()) {
            String url = "/api/task-domain/" + taskDomainDTO.getId();
            HttpHeaders httpHeaders = getDefaultHttpHeaders();
            HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
            when(getPersonalExamConnector().taskDomainIsUsed(anyLong())).thenReturn(true);
            ResponseEntity<String> responseEntity = doPut(url, httpEntity, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        } else if (taskDomainDTO == null) {
            String url = "/api/task-domain/123";
            HttpHeaders httpHeaders = getDefaultHttpHeaders();
            HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
            ResponseEntity<String> responseEntity = doPut(url, httpEntity, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        } else {
            String url = "/api/task-domain/" + taskDomainDTO.getId();
            HttpHeaders httpHeaders = getDefaultHttpHeaders();
            HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
            ResponseEntity<TaskDomain> responseEntity = doPut(url, httpEntity, TaskDomain.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }

    @When("^system generate image of scheme and save in \"(.*)\" task domain$")
    public void requestSentCreateImageForTaskDomain(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, TestExecutionContext.getTestContext().getDisciplineId());
        String url = "/api/task-domain/" + taskDomainDTO.getId() + "/puml";
        String puml = "@startuml\n" + "Bob -> Alice : hello\n" + "@enduml\n";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(puml, httpHeaders);
        ResponseEntity<String> responseEntity = doPut(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system generate image of scheme and save in \"(.*)\" task domain with wrong script$")
    public void requestSentCreateImageForTaskDomainWithWrongScript(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, TestExecutionContext.getTestContext().getDisciplineId());
        String url = "/api/task-domain/" + taskDomainDTO.getId() + "/puml";
        String puml = "error";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(puml, httpHeaders);
        ResponseEntity<String> responseEntity = doPut(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system retrieve information about \"(.*)\" task domain tasks count")
    public void systemRetrieveInformationAboutTaskDomainTasksCount(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, TestExecutionContext.getTestContext().getDisciplineId());
        String url = "/api/task-domain/" + taskDomainDTO.getId() + "/tasks-count";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<TaskDomainModel> responseEntity = doGet(url, httpEntity, TaskDomainModel.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
