package com.sytoss.lessons.services;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.connectors.GroupConnector;
import com.sytoss.lessons.convertors.GroupConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GroupServiceTest extends AbstractJunitTest {

    @Mock
    private GroupConnector groupConnector;

    @Mock
    private GroupConvertor groupConvertor;

    @InjectMocks
    private GroupService groupService;

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
