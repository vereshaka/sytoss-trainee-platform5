package com.sytoss.lessons.services;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.GroupReferenceConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.mockito.Mockito.*;

class GroupServiceTest extends StpUnitTest {

    @Mock
    private GroupReferenceConnector groupConnector;

    @Spy
    private DisciplineConvertor disciplineConvertor;

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

        DisciplineDTO disciplineDTO1 = new DisciplineDTO();
        disciplineDTO1.setName("SQL");
        disciplineDTO1.setTeacherId(1L);
        disciplineDTO1.setId(1L);

        DisciplineDTO disciplineDTO2 = new DisciplineDTO();
        disciplineDTO2.setName("Mongo");
        disciplineDTO2.setTeacherId(1L);
        disciplineDTO2.setId(2L);

        GroupReferenceDTO group1 = new GroupReferenceDTO(1L, disciplineDTO1, false);
        GroupReferenceDTO group2 = new GroupReferenceDTO(2L, disciplineDTO1, false);
        GroupReferenceDTO group3 = new GroupReferenceDTO(1L, disciplineDTO2, false);
        List<GroupReferenceDTO> input = List.of(group1, group2, group3);
        when(groupConnector.findByDisciplineId_TeacherId(1L)).thenReturn(input);
        List<Group> groups = groupService.findGroups();
        verify(disciplineConvertor, times(3)).fromDTO(any(), any());
        Assertions.assertEquals(2, groups.size());
    }
}
