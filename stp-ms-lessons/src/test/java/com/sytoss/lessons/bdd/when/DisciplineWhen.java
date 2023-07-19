package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.Mockito.when;

public class DisciplineWhen extends CucumberIntegrationTest {

    @When("^teacher creates \"(.*)\" discipline$")
    public void disciplineCreating(String disciplineName) {
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", TestExecutionContext.getTestContext().getTeacherId().intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", disciplineName);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Discipline> responseEntity = doPost("/api/discipline", httpEntity, Discipline.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher creates existing \"(.*)\" discipline$")
    public void existingDisciplineCreating(String disciplineName) {
        String url = "/api/discipline";
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(TestExecutionContext.getTestContext().getToken());

        HttpEntity<Discipline> httpEntity = new HttpEntity<>(discipline, httpHeaders);
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", TestExecutionContext.getTestContext().getTeacherId().intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);
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
        String url = "/api/disciplines/my";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", teacherId.intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);
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

    @When("^link this discipline to group with id (.*)")
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

    @When("student receive his disciplines")
    public void studentReceiveHisDisciplines() {
        String url = "/api/disciplines/my";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        when(getUserConnector().findMyGroupId()).thenReturn(TestExecutionContext.getTestContext().getGroupId());
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<List<Discipline>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
