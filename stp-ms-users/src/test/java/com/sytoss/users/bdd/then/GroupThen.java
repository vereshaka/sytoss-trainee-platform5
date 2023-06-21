package com.sytoss.users.bdd.then;

import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import io.cucumber.java.en.Then;
import jakarta.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
public class GroupThen extends CucumberIntegrationTest {

    @Then("^this student should have \"(.*)\" group$")
    public void studentShouldHasGroup(String groupName) {
        StudentDTO studentDTO = (StudentDTO) getUserConnector().findById(TestExecutionContext.getTestContext().getUser().getId()).orElseThrow();
        List<GroupDTO> filter = studentDTO.getGroups().stream().filter(item -> item.getName().equals(groupName)).toList();
        assertEquals(1, filter.size());

    }
}
