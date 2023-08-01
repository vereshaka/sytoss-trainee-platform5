package com.sytoss.lessons.services;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.GroupReferenceConnector;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

class GroupServiceTest extends StpUnitTest {

    @Mock
    private GroupReferenceConnector groupConnector;
    @InjectMocks
    private GroupService groupService;

    @Test
    public void findGroups() {
        Teacher user = new Teacher();
        user.setId(1L);
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", user).build();
        Object credential = null;
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, credential);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName("SQL");
        disciplineDTO.setTeacherId(1L);
        disciplineDTO.setId(1L);

        GroupReferenceDTO group = new GroupReferenceDTO(1L, disciplineDTO);
        List<GroupReferenceDTO> input = new ArrayList<>();
        input.add(group);
        when(groupConnector.findByDisciplineId_TeacherId(1L)).thenReturn(input);
        List<Group> groups = groupService.findGroups();
        Assertions.assertEquals(1, groups.size());
    }
}
