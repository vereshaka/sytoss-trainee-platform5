package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.connectors.GroupConnector;
import com.sytoss.lessons.convertors.GroupConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GroupServiceTest extends AbstractJunitTest {

    @Mock
    private GroupConnector groupConnector;

    @Mock
    private GroupConvertor groupConvertor;

    @InjectMocks
    private GroupService groupService;

    @Mock
    private DisciplineService disciplineService;

    @Test
    public void getMyGroups() {
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

    @Test
    public void shouldCreateGroup() {
        Discipline discipline = new Discipline();
        discipline.setId(1L);
        discipline.setName("SQL");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        discipline.setTeacher(teacher);
        when(disciplineService.getById(1L)).thenReturn(discipline);
        when(groupConnector.getByNameAndDisciplineId("Test", 1L)).thenReturn(null);
        Mockito.doAnswer((Answer<GroupDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            GroupDTO result = (GroupDTO) args[0];
            result.setId(1L);
            return result;
        }).when(groupConnector).save(any(GroupDTO.class));
        Group group = new Group();
        group.setName("Test");
        when(disciplineService.getById(1L)).thenReturn(discipline);
        Group result = groupService.create(discipline.getId(), group);
        assertEquals(group.getName(), result.getName());
        assertEquals(discipline.getId(), result.getDiscipline().getId());
    }

    @Test
    public void shouldNotCreateGroupWhenItExists() {
        when(groupConnector.getByNameAndDisciplineId(any(),any())).thenReturn(new GroupDTO());
        Group group = new Group();
        group.setName("Test");
        assertThrows(GroupExistException.class, () -> groupService.create(1L, group));
    }
}
