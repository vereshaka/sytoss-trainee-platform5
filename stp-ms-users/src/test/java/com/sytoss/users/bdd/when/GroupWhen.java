package com.sytoss.users.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Slf4j
public class GroupWhen extends CucumberIntegrationTest {

    @When("^teacher assignee this student to \"(.*)\" group$")
    public void assigneeStudentToGroup(String groupName) throws JOSEException {
        Long groupId;
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        UserDTO studentDTO = TestExecutionContext.getTestContext().getUser();
        Student student = new Student();
        getUserConverter().fromDTO(studentDTO, student);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Student> httpEntity = new HttpEntity<>(student, httpHeaders);
        if (groupDTO == null) {
            groupId = 99L;
            String url = "/api/group/" + groupId + "/student";
            ResponseEntity<String> responseEntity = doPost(url, httpEntity, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        } else {
            groupId = groupDTO.getId();
            String url = "/api/group/" + groupId + "/student";
            ResponseEntity<Student> responseEntity = doPost(url, httpEntity, Student.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }
}
