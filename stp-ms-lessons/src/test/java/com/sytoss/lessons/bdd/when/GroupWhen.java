package com.sytoss.lessons.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.when;

@Slf4j
public class GroupWhen extends CucumberIntegrationTest {

    @When("receive all groups by discipline with id {long}")
    public void requestSentFindGroupsByDiscipline(Long disciplineId) {
        String url = "/api/discipline/" + disciplineId + "/groups";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Group> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<List<Group>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<List<Group>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher \"(.*)\" \"(.*)\" create \"(.*)\" group for \"(.*)\" discipline$")
    public void requestSentCreateGroup(String firstName, String lastName, String groupName, String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        String url = "/api/discipline/" + disciplineDTO.getId() + "/group";
        Group group = new Group();
        group.setName(groupName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Group> httpEntity = new HttpEntity<>(group, httpHeaders);
        ResponseEntity<Group> responseEntity = doPost(url, httpEntity, Group.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher \"(.*)\" \"(.*)\" create \"(.*)\" group for \"(.*)\" discipline that exists$")
    public void requestSentCreateGroupThatExist(String firstName, String lastName, String groupName, String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        String url = "/api/discipline/" + disciplineDTO.getId() + "/group";
        Group group = new Group();
        group.setName(groupName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Group> httpEntity = new HttpEntity<>(group, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^receive all groups by teacher with id (.*)")
    public void receiveAllGroupsByTeacherWithId(String teacherKey) {
        String url = "/api/teacher/my/groups";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"),"123","123","123","123"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        Teacher teacher = new Teacher();
        teacher.setId(TestExecutionContext.getTestContext().getIdMapping().get(teacherKey));
        when(getUserConnector().getMyProfile()).thenReturn(teacher);
        ResponseEntity<List<Group>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}