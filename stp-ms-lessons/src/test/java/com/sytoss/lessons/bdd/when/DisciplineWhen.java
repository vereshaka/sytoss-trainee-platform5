package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.controllers.api.ResponseObject;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.Mockito.when;

public class DisciplineWhen extends LessonsIntegrationTest {

    @When("^teacher creates \"(.*)\" discipline$")
    public void disciplineCreating(String disciplineName) {
        String url = "/api/discipline";

        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", getTestExecutionContext().getDetails().getTeacherId().intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", disciplineName);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Discipline> responseEntity = doPost(url, httpEntity, Discipline.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher creates existing \"(.*)\" discipline$")
    public void existingDisciplineCreating(String disciplineName) {
        String url = "/api/discipline";

        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", getTestExecutionContext().getDetails().getTeacherId().intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", disciplineName);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Discipline> responseEntity = doPost(url, httpEntity, Discipline.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^receive \"(.*)\" discipline information$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        String url = "/api/discipline/" + discipline.getId();
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Discipline> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<Discipline> responseEntity = doGet(url, requestEntity, Discipline.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher with id (.*) retrieve his disciplines$")
    public void requestSentReceiveDisciplinesByTeacher(Long teacherId) {
        String url = "/api/teacher/my/disciplines/0/5";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", teacherId.intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);
        ResponseEntity<ResponseObject<Discipline>> responseEntity = doPost(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("receive all disciplines")
    public void requestSentFindAllDisciplines() {
        String url = "/api/disciplines";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^link this discipline to group with id (.*)")
    public void linkDisciplineToGroup(Long groupId) {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/group/" + groupId;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity = doPost(url, httpEntity, Void.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^receive this discipline's icon$")
    public void getDisciplineIcon() {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/icon";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> responseEntity = doGet(url, requestEntity, byte[].class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("student receive his disciplines")
    public void studentReceiveHisDisciplines() {
        String url = "/api/disciplines/my";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        //TODO: yevgenyv: we should take token from execution context
        httpHeaders.setBearerAuth(generateJWT(new ArrayList<>(), "John", "Johnson", "test@test.com", "Student"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        when(getUserConnector().findMyGroupId()).thenReturn(getTestExecutionContext().getDetails().getGroupId());
        ResponseEntity<List<Discipline>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^a teacher delete discipline with id (.*)$")
    public void teacherDeleteDiscipline(String id) {
        Long disciplineId = (Long) getTestExecutionContext().replaceId(id);
        String url = "/api/discipline/"+disciplineId+"/delete";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Discipline> responseEntity = doDelete(url, httpEntity, Discipline.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @Given("^\"(.*)\" discipline exists with id (.*)$")
    public void disciplineExist(String disciplineName, String id) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
            disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
            disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            getTestExecutionContext().registerId(id, disciplineDTO.getId());
        }
    }
}
