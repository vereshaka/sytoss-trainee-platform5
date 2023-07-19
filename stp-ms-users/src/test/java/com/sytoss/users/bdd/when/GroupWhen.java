package com.sytoss.users.bdd.when;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
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
}
