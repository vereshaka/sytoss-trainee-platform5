package com.sytoss.users.bdd.when;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
public class GroupWhen extends UsersIntegrationTest {

    @When("^teacher assignee this student to \"(.*)\" group$")
    public void assigneeStudentToGroup(String groupName) {
        Long groupId;
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        UserDTO studentDTO = getTestExecutionContext().getDetails().getUser();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), "s"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        if (groupDTO == null) {
            groupId = 99L;
            String url = "/api/group/" + groupId + "/student/" + getTestExecutionContext().getDetails().getUser().getUid();
            ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
            getTestExecutionContext().setResponse(responseEntity);
        } else {
            groupId = groupDTO.getId();
            String url = "/api/group/" + groupId + "/student/" + getTestExecutionContext().getDetails().getUser().getUid();
            ResponseEntity<Student> responseEntity = doPost(url, httpEntity, Student.class);
            getTestExecutionContext().setResponse(responseEntity);
        }
    }

    @When("^\"(.*)\" group is created$")
    public void requestSentCreateGroupThatExist(String groupName) {
        String url = "/api/group";
        Group group = new Group();
        group.setName(groupName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        UserDTO studentDTO = getTestExecutionContext().getDetails().getUser();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), "s"));
        HttpEntity<Group> httpEntity = new HttpEntity<>(group, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^system retrieve information about \"(.*)\" group")
    public void systemRetrieveInformationAboutGroup(String groupName) {
        GroupDTO group = getGroupConnector().getByName(groupName);
        String url = "/api/group/"+group.getId();
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        UserDTO studentDTO = getTestExecutionContext().getDetails().getUser();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), "s"));
        HttpEntity<Group> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Group> responseEntity = doGet(url, httpEntity, Group.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^system retrieve information about students of the \"(.*)\" group")
    public void systemRetrieveInformationAboutStudentsOfTheGroup(String groupName) {
        GroupDTO group = getGroupConnector().getByName(groupName);
        String url = "/api/group/"+group.getId()+"/students";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        UserDTO studentDTO = getTestExecutionContext().getDetails().getUser();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), "s"));
        HttpEntity<Group> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Student>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {});
        getTestExecutionContext().setResponse(responseEntity);
    }
}
