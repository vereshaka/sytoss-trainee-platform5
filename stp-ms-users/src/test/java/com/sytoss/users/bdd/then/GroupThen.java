package com.sytoss.users.bdd.then;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import io.cucumber.java.en.Then;
import jakarta.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
public class GroupThen extends UsersIntegrationTest {

    @Then("^this student should have \"(.*)\" group$")
    public void studentShouldHasGroup(String groupName) {
        StudentDTO studentDTO = (StudentDTO) getUserConnector().getByUid(getTestExecutionContext().getDetails().getUser().getUid());
        List<GroupDTO> filter = studentDTO.getGroups().stream().filter(item -> item.getName().equals(groupName)).toList();
        assertEquals(1, filter.size());
    }

    @Then("^\"(.*)\" group should exist$")
    public void groupShouldBe(String groupName) {
        GroupDTO groupDTO = getGroupConnector().getByName(groupName);
        assertNotNull(groupDTO);
    }

    @Then("^group name should be \"(.*)\"")
    public void groupNameShouldBe(String groupName) {
        Group group = (Group) getTestExecutionContext().getResponse().getBody();
        assertEquals(groupName,group.getName());
    }

    @Then("^this group should have students")
    public void thisGroupShouldHaveStudents(List<StudentDTO> studentDTOList) {
        List<Student> students = (List<Student>) getTestExecutionContext().getResponse().getBody();
        assertNotNull(students.size());
        assertEquals(studentDTOList.size(),students.size());
        int count = 0;
        for (StudentDTO studentDTO : studentDTOList) {
            for (Student student : students) {
                if (student.getFirstName().equals(studentDTO.getFirstName()) && student.getLastName().equals(studentDTO.getLastName())) {
                    count++;
                }
            }
        }
        assertEquals(students.size(), count);
    }


}
