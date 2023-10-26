package com.sytoss.users.bdd.then;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class UserThen extends UsersIntegrationTest {

    @Then("^teacher with \"(\\w+)\" firstname and \"(\\w+)\" lastname should exist$")
    public void teacherShouldBeSave(String firstName, String lastname) {
        Teacher teacher = (Teacher) getTestExecutionContext().getResponse().getBody();
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(lastname, teacher.getLastName());
    }

    @Then("^this user has photo")
    public void userHasPhoto() {
        UserDTO userDto = getUserConnector().getByEmail(getTestExecutionContext().getDetails().getUser().getEmail());
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
        List<Group> responseGroupList = (List<Group>) getTestExecutionContext().getResponse().getBody();
        Assertions.assertEquals(groupList.size(), responseGroupList.size());
        Assertions.assertTrue(groupList.stream()
                .allMatch(expectedGroup -> Objects.requireNonNull(responseGroupList).stream()
                        .anyMatch(actualGroup -> actualGroup.getName().equals(expectedGroup.getName())
                        )
                )
        );
    }

    @Then("should return photo")
    public void shouldReturnPhoto() {
        byte[] photo = getTestExecutionContext().getDetails().getUser().getPhoto();
        byte[] userPhoto = (byte[]) getTestExecutionContext().getResponse().getBody();
        assertNotNull(userPhoto);
        assertArrayEquals(photo, userPhoto);
    }

    @Then("^student photo should be received$")
    public void userPhotoShouldBeReceived() {
        UserDTO studentDTO = getUserConnector().getByUid(getTestExecutionContext().getDetails().getUser().getUid());
        byte[] photo = (byte[]) getTestExecutionContext().getResponse().getBody();
        assertEquals(studentDTO.getPhoto().length, photo.length);
    }
}
