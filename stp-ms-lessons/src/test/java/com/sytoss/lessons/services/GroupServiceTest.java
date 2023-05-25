package com.sytoss.lessons.services;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.AbstractLessonsApplicationTest;
import com.sytoss.lessons.connectors.GroupConnector;
import com.sytoss.lessons.convertors.GroupConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GroupServiceTest extends AbstractLessonsApplicationTest {

    @InjectMocks
    private GroupService groupService;

    @MockBean
    private GroupConnector groupConnector;

    @MockBean
    private GroupConvertor groupConvertor;

    @Test
    public void findGroups() {
        GroupDTO group = new GroupDTO();
        group.setId(11L);
        group.setName("Test");
        DisciplineDTO discipline = new DisciplineDTO();
        discipline.setId(11L);
        discipline.setName("SQL");
        group.setDiscipline(discipline);
        List<GroupDTO> input = new ArrayList<>();
        input.add(group);
        input.add(group);
        when(groupConnector.findByDisciplineId(any())).thenReturn(input);
        List<Group> result = groupService.findByDiscipline(11L);
        assertEquals(input.size(), result.size());
    }
}
