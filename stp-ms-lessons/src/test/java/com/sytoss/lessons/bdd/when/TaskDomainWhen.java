package com.sytoss.lessons.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TaskDomainWhen extends CucumberIntegrationTest {

    @When("^system create \"(.*)\" task domain$")
    public void requestSentCreateTaskDomain(String name) throws JOSEException {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(name);
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("create_task_domain")));
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
        ResponseEntity<TaskDomain> responseEntity = doPost(url, httpEntity, TaskDomain.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system create \"(.*)\" task domain when it exist$")
    public void requestSentCreateTaskDomainWhenItExist(String name) throws JOSEException {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(name);
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("create_task_domain")));
        HttpEntity<TaskDomain> httpEntity = new HttpEntity<>(taskDomain, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system retrieve information about \"(.*)\" task domain$")
    public void systemTryToFindTaskDomainById(String taskDomainName) throws JOSEException {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, TestExecutionContext.getTestContext().getDisciplineId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("get_task_domain")));
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        if (taskDomainDTO == null) {
            String url = "/api/taskDomain/123";
            ResponseEntity<String> responseEntity = doGet(url, requestEntity, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
        if (taskDomainDTO != null) {
            String url = "/api/taskDomain/" + taskDomainDTO.getId();
            ResponseEntity<TaskDomain> responseEntity = doGet(url, requestEntity, TaskDomain.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }

    @When("^receive all task domains by \"(.*)\" discipline$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) throws JOSEException {
        DisciplineDTO discipline = getDisciplineConnector().getByName(disciplineName);
        String url = "/api/discipline/" + discipline.getId() + "/taskDomains";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("get_task_domains_by_discipline")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<TaskDomain>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<List<TaskDomain>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
