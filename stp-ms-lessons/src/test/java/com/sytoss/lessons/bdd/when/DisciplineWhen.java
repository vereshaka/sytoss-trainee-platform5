package com.sytoss.lessons.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.when;

public class DisciplineWhen extends CucumberIntegrationTest {

    @When("^teacher creates \"(.*)\" discipline$")
    public void disciplineCreating(String disciplineName) throws JOSEException {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);

        HttpEntity<Discipline> httpEntity = new HttpEntity<>(discipline);
        ResponseEntity<Discipline> responseEntity = doPost(url, httpEntity, new ParameterizedTypeReference<Discipline>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher creates existing \"(.*)\" discipline$")
    public void existingDisciplineCreating(String disciplineName) throws JOSEException {
        String url = "/api/teacher/" + TestExecutionContext.getTestContext().getTeacherId() + "/discipline";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<Discipline> httpEntity = new HttpEntity<>(discipline, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^receive \"(.*)\" discipline information$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) throws JOSEException {
        DisciplineDTO discipline = getDisciplineConnector().getByName(disciplineName);
        String url = "/api/discipline/" + discipline.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Discipline> responseEntity = doGet(url, requestEntity, Discipline.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher with id (.*) retrieve his disciplines$")
    public void requestSentReceiveDisciplinesByTeacher(Long teacherId) throws JOSEException {
        String url = "/api/teacher/my/disciplines";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        when(getUserConnector().getMyProfile()).thenReturn(teacher);
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<List<Discipline>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("receive all disciplines")
    public void requestSentFindAllDisciplines() throws JOSEException {
        String url = "/api/disciplines";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<List<Discipline>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
