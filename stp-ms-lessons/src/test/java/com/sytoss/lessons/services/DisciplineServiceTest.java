package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.*;
import com.sytoss.lessons.dto.*;
import com.sytoss.stp.test.StpUnitTest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DisciplineServiceTest extends StpUnitTest {

    @Mock
    private DisciplineConnector disciplineConnector;

    @Mock
    private TaskConnector taskConnector;

    @Mock
    private GroupReferenceConnector groupReferenceConnector;

    @InjectMocks
    private DisciplineService disciplineService;

    @Spy
    private DisciplineConvertor disciplineConvertor = new DisciplineConvertor();

    @Spy
    private TaskDomainConvertor taskDomainConvertor = new TaskDomainConvertor(disciplineConvertor);

    @Spy
    private TopicConvertor topicConvertor = new TopicConvertor(disciplineConvertor);

    @Spy
    private TaskConditionConvertor taskConditionConvertor;

    @Spy
    private TaskConvertor taskConvertor =
            new TaskConvertor(taskDomainConvertor, taskConditionConvertor, topicConvertor);

    @Mock
    private UserConnector userConnector;

    @Mock
    private TopicConnector topicConnector;

    @Test
    public void shouldSaveDiscipline() {
        Teacher user = new Teacher();
        user.setId(1L);
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", user).build();
        Object credential = null;
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, credential);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(disciplineConnector.getByNameAndTeacherId("SQL", 1L)).thenReturn(null);
        Mockito.doAnswer((Answer<DisciplineDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            DisciplineDTO result = (DisciplineDTO) args[0];
            result.setId(1L);
            return result;
        }).when(disciplineConnector).saveAndFlush(any(DisciplineDTO.class));
        Discipline input = new Discipline();
        input.setName("SQL");
        Discipline result = disciplineService.create(input);
        assertEquals(1L, result.getTeacher().getId());
        assertEquals("SQL", result.getName());
    }

    @Test
    public void shouldRetrieveDisciplinesByStudent() {
        DisciplineDTO discipline = new DisciplineDTO();
        discipline.setId(11L);
        discipline.setName("Test1");
        when(userConnector.findMyGroupId()).thenReturn(List.of(1L, 2L));
        when(disciplineConnector.findByGroupReferencesGroupId(anyLong())).thenReturn(List.of(discipline));
        List<Discipline> disciplineList = disciplineService.findAllMyDiscipline();
        assertEquals(2, disciplineList.size());
    }

    public Teacher createReference(Long id) {
        Teacher result = new Teacher();
        result.setId(id);
        result.setFirstName("Firstname");
        result.setLastName("Lastname");
        return result;
    }

    @Test
    public void getDisciplineById() {
        DisciplineDTO input = new DisciplineDTO();
        input.setId(1L);
        input.setName("SQL");
        input.setTeacherId(1L);
        when(disciplineConnector.getReferenceById(any())).thenReturn(input);
        Discipline result = disciplineService.getById(1L);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getName(), result.getName());
    }

    @Test
    public void shouldRaiseExceptionWhenDisciplineNotExist() {
        when(disciplineConnector.getReferenceById(1L)).thenThrow(new EntityNotFoundException());
        assertThrows(DisciplineNotFoundException.class, () -> disciplineService.getById(1L));
    }

    @Test
    public void shouldReturnDisciplinesByTeacher() {
        Teacher user = new Teacher();
        user.setId(1L);
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", user).build();
        Object credential = null;
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, credential);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(1L);
        disciplineDTO.setName("SQL");
        disciplineDTO.setTeacherId(1L);
        List<DisciplineDTO> disciplines = List.of(disciplineDTO);
        Page<DisciplineDTO> input = new PageImpl<>(disciplines);
        when(disciplineConnector.findByTeacherIdOrderByCreationDateDesc(1L, PageRequest.of(1,1))).thenReturn(input);
        Page<Discipline> result = disciplineService.findDisciplines(1,1);
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void shouldFindAllDisciplines() {
        DisciplineDTO discipline1 = new DisciplineDTO();
        discipline1.setId(11L);
        discipline1.setName("Test1");
        DisciplineDTO discipline2 = new DisciplineDTO();
        discipline2.setId(12L);
        discipline2.setName("Test2");
        List<DisciplineDTO> input = List.of(discipline1, discipline2);
        when(disciplineConnector.findAll()).thenReturn(input);
        List<Discipline> result = disciplineService.findAllDisciplines();
        assertEquals(input.size(), result.size());
    }

    @Test
    public void testAssignGroupToDiscipline() {
        Long disciplineId = 1L;
        Long groupId = 1L;
        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        when(disciplineConnector.getReferenceById(anyLong())).thenReturn(mock(DisciplineDTO.class));
        disciplineService.assignGroupToDiscipline(disciplineId, groupId);
        verify(groupReferenceConnector).save(any(GroupReferenceDTO.class));
    }

    @Test
    public void getGroups() {
        when(disciplineConnector.getReferenceById(anyLong())).thenReturn(mock(DisciplineDTO.class));
        List<GroupReferenceDTO> groupReferenceDTOS = new ArrayList<>(List.of(mock(GroupReferenceDTO.class), mock(GroupReferenceDTO.class)));
        when(groupReferenceConnector.findByDisciplineId(anyLong())).thenReturn(groupReferenceDTOS);
        List<Group> resultList = disciplineService.getGroups(5L);
        assertEquals(2, resultList.size());
    }

    @Test
    public void testGetIcon() {
        Long disciplineId = 1L;
        byte[] iconBytes = {0x01, 0x02, 0x03};
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(disciplineId);
        disciplineDTO.setIcon(iconBytes);
        when(disciplineConnector.getReferenceById(anyLong())).thenReturn(disciplineDTO);
        byte[] result = disciplineService.getIcon(disciplineId);
        assertEquals(iconBytes, result);
    }

    @Test
    public void shouldFindTasksByDisciplineId() {
        Teacher user = new Teacher();
        user.setId(1L);
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", user).build();
        Object credential = null;
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, credential);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<TaskDTO> input = new ArrayList<>();
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName("d1");
        TaskDomainDTO domainDTO = new TaskDomainDTO();
        domainDTO.setDiscipline(disciplineDTO);
        taskDTO.setTaskDomain(domainDTO);
        input.add(taskDTO);
        when(taskConnector.getByTaskDomainDisciplineId(1L)).thenReturn(input);
        List<Task> result = disciplineService.findTasksByDisciplineId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void assignGroupsToDiscipline() {
        GroupsIds groupsIds = new GroupsIds();
        groupsIds.setGroupsIds(List.of(1L, 2L));
        Long disciplineId = 1L;
        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        when(disciplineConnector.getReferenceById(anyLong())).thenReturn(mock(DisciplineDTO.class));
        when(topicConnector.countDurationByDisciplineId(any())).thenReturn(1.0);
        disciplineService.assignGroupsToDiscipline(disciplineId, groupsIds.getGroupsIds());
        verify(groupReferenceConnector, times(groupsIds.getGroupsIds().size())).save(any(GroupReferenceDTO.class));
    }
}
