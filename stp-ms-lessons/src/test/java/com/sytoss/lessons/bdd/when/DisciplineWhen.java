package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.when;

public class DisciplineWhen extends CucumberIntegrationTest {

    @When("^teacher creates \"(.*)\" discipline$")
    public void disciplineCreating(String disciplineName) {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Discipline> httpEntity = new HttpEntity<>(discipline, httpHeaders);
        ResponseEntity<Discipline> responseEntity = doPost(url, httpEntity, Discipline.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher creates existing \"(.*)\" discipline$")
    public void existingDisciplineCreating(String disciplineName) {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Discipline> httpEntity = new HttpEntity<>(discipline, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^receive \"(.*)\" discipline information$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        String url = "/api/discipline/" + discipline.getId();
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Discipline> responseEntity = doGet(url, requestEntity, Discipline.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher with id (.*) retrieve his disciplines$")
    public void requestSentReceiveDisciplinesByTeacher(Long teacherId) {
        String url = "/api/teacher/my/disciplines";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        when(getUserConnector().getMyProfile()).thenReturn(teacher);
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("receive all disciplines")
    public void requestSentFindAllDisciplines() {
        String url = "/api/disciplines";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("link this discipline to group with id {long}")
    public void linkDisciplineToGroup(Long groupId) {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/group/" + groupId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity = doPost(url, httpEntity, Void.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^receive this discipline's icon$")
    public void getDisciplineIcon() {
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId() + "/icon";
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> responseEntity = doGet(url, requestEntity, byte[].class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
