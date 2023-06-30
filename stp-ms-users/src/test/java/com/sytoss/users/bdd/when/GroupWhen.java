package com.sytoss.users.bdd.when;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import com.sytoss.users.dto.GroupDTO;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Slf4j
public class GroupWhen extends CucumberIntegrationTest {

    @When("^teacher assignee this student to \"(.*)\" group$")
    public void assigneeStudentToGroup(String groupName) {
        Long groupId;
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        if (groupDTO == null) {
            groupId = 99L;
            String url = "/api/group/" + groupId + "/student/" + TestExecutionContext.getTestContext().getUser().getEncryptedId();
            ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        } else {
            groupId = groupDTO.getId();
            String url = "/api/group/" + groupId + "/student/" + TestExecutionContext.getTestContext().getUser().getEncryptedId();
            ResponseEntity<Student> responseEntity = doPost(url, httpEntity, Student.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }

    @When("^\"(.*)\" group is created$")
    public void requestSentCreateGroupThatExist(String groupName) {
        String url = "/api/group/";
        Group group = new Group();
        group.setName(groupName);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Group> httpEntity = new HttpEntity<>(group, httpHeaders);
        ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
