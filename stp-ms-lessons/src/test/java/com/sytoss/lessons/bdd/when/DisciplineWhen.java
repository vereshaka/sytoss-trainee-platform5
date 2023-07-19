package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.stp.test.cucumber.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.Mockito.when;

public class DisciplineWhen extends LessonsIntegrationTest {

    @When("^teacher creates \"(.*)\" discipline$")
    public void disciplineCreating(String disciplineName) {
        String url = "/api/discipline";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        HttpHeaders httpHeaders =getDefaultHttpHeaders();
        HttpEntity<Discipline> httpEntity = new HttpEntity<>(discipline, httpHeaders);
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", getTestExecutionContext().getDetails().getTeacherId().intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);
        ResponseEntity<Discipline> responseEntity = doPost(url, httpEntity, Discipline.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher creates existing \"(.*)\" discipline$")
    public void existingDisciplineCreating(String disciplineName) {
        String url = "/api/discipline";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(getTestExecutionContext().getDetails().getToken());
        HttpEntity<Discipline> httpEntity = new HttpEntity<>(discipline, httpHeaders);
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", getTestExecutionContext().getDetails().getTeacherId().intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^receive \"(.*)\" discipline information$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        String url = "/api/discipline/" + discipline.getId();
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Discipline> responseEntity = doGet(url, requestEntity, Discipline.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher with id (.*) retrieve his disciplines$")
    public void requestSentReceiveDisciplinesByTeacher(Long teacherId) {
        String url = "/api/disciplines/my";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"),"","","",""));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", teacherId.intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("receive all disciplines")
    public void requestSentFindAllDisciplines() {
        String url = "/api/disciplines";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"),"","","",""));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^link this discipline to group with id (.*)")
    public void linkDisciplineToGroup(Long groupId) {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/group/" + groupId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"),"","","",""));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity = doPost(url, httpEntity, Void.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^receive this discipline's icon$")
    public void getDisciplineIcon() {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/icon";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"),"","","",""));
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> responseEntity = doGet(url, requestEntity, byte[].class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("student receive his disciplines")
    public void studentReceiveHisDisciplines() {
        String url = "/api/disciplines/my";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        //httpHeaders.setBearerAuth(generateJWT(getTestExecutionContext().getDetails().getToken());
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        when(getUserConnector().findMyGroupId()).thenReturn(getTestExecutionContext().getDetails().getGroupId());
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }
}
