package com.sytoss.users.bdd.then;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserThen extends CucumberIntegrationTest {

    @Then("^teacher with \"(\\w+)\" firstname and \"(\\w+)\" lastname should exist$")
    public void teacherShouldBeSave(String firstName, String lastname) {
        Teacher teacher = (Teacher) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(lastname, teacher.getLastName());
    }

    @Then("^this user has photo")
    public void userHasPhoto(){
        UserDTO userDto = getUserConnector().getByEmail(TestExecutionContext.getTestContext().getUser().getEmail());
        assertNotNull(userDto.getPhoto());
    }

    @Then("should receive information about group of student")
    public void shouldReceiveGroupsByStudent(List<GroupDTO> groupDTOList) {
        List<Group> groupList = new ArrayList<>();
        groupDTOList.forEach(groupDTO -> {
            Group group = new Group();
            getGroupConvertor().fromDTO(groupDTO, group);
            groupList.add(group);
        });
        List<Group> responseGroupList = (List<Group>) TestExecutionContext.getTestContext().getResponse().getBody();
        Assertions.assertEquals(groupList.size(), responseGroupList.size());
        Assertions.assertTrue(groupList.stream()
                .allMatch(expectedGroup -> Objects.requireNonNull(responseGroupList).stream()
                        .anyMatch(actualGroup -> actualGroup.getName().equals(expectedGroup.getName())
                        )
                )
        );
    }
    @Then("should receive information about discipline of student")
    public void shouldReceiveDisciplineByStudent(List<Discipline> disciplines) {
        List<Discipline> responseDisciplineList = (List<Discipline>) TestExecutionContext.getTestContext().getResponse().getBody();
        Assertions.assertEquals(disciplines.size(), responseDisciplineList.size());
//        Iterator<Discipline> disciplineIterator = responseDisciplineList.iterator();
//        while (disciplineIterator.hasNext()){
//            Discipline discipline = disciplineIterator.next();
//            for(Discipline discipline1 : disciplines){
//                if(discipline1.getName().equals(discipline.getName())){
//
//                }
//            }
//        }
//        assertEquals(0, responseDisciplineList.size());
    }
}
