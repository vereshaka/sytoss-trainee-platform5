package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
public class GroupWhen extends CucumberIntegrationTest {

    @When("^receive all groups by \"(.*)\" discipline$")
    public void requestSentFindGroupsByDiscipline(String disciplineName) {
        DisciplineDTO discipline = getDisciplineConnector().getByName(disciplineName);
        String url = "/api/discipline/" + discipline.getId() + "/groups";
        ResponseEntity<List<Group>> responseEntity = doGet(url, null, new ParameterizedTypeReference<List<Group>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher \"(.*)\" \"(.*)\" create \"(.*)\" group for \"(.*)\" discipline$")
    public void requestSentCreateGroup(String firstName, String lastName, String groupName, String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        String url = "/api/discipline/" + disciplineDTO.getId() + "/group";
        Group group = new Group();
        group.setName(groupName);
        ResponseEntity<Group> responseEntity = doPost(url, group, Group.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^teacher \"(.*)\" \"(.*)\" create \"(.*)\" group for \"(.*)\" discipline that exists$")
    public void requestSentCreateGroupThatExist(String firstName, String lastName, String groupName, String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        String url = "/api/discipline/" + disciplineDTO.getId() + "/group";
        Group group = new Group();
        group.setName(groupName);
        ResponseEntity<String> responseEntity = doPost(url, group, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}