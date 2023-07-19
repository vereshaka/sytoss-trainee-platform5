package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.Mockito.when;

@Slf4j
public class GroupWhen extends LessonsIntegrationTest {

    @When("^receive all groups by discipline with id (.*)$")
    public void requestSentFindGroupsByDiscipline(String disciplineId) {
        String url = "/api/discipline/" + getTestExecutionContext().getIdMapping().get(disciplineId) + "/groups";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Group> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<List<Group>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^receive all groups by teacher with id (.*)")
    public void receiveAllGroupsByTeacherWithId(String teacherKey) {
        String url = "/api/teacher/my/groups";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), "123", "123", "123", "123"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", getTestExecutionContext().getIdMapping().get(teacherKey).intValue());
        when(getUserConnector().getMyProfile()).thenReturn(teacherMap);
        ResponseEntity<List<GroupReferenceDTO>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }
}