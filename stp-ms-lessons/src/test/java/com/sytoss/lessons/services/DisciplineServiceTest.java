package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.connectors.GroupReferenceConnector;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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

public class DisciplineServiceTest extends AbstractApplicationTest {

    @MockBean
    private DisciplineConnector disciplineConnector;

    @MockBean
    private GroupReferenceConnector groupReferenceConnector;

    @InjectMocks
    @Autowired
    private DisciplineService disciplineService;

    @Test
    public void shouldSaveDiscipline() {
        when(disciplineConnector.getByNameAndTeacherId("SQL", 1L)).thenReturn(null);
        Mockito.doAnswer((Answer<DisciplineDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            DisciplineDTO result = (DisciplineDTO) args[0];
            result.setId(1L);
            return result;
        }).when(disciplineConnector).saveAndFlush(any(DisciplineDTO.class));
        Discipline input = new Discipline();
        input.setName("SQL");
        Discipline result = disciplineService.create(1L, input);
        assertEquals(1L, result.getTeacher().getId());
        assertEquals("SQL", result.getName());
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
    public void shouldReturnDisciplinesByTeacher(){
        Teacher user = new Teacher();
        user.setId(1L);
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", user).build();
        Object credential = null;
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, credential);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<DisciplineDTO> input = new ArrayList<>();
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(1L);
        disciplineDTO.setName("SQL");
        disciplineDTO.setTeacherId(1L);
        input.add(disciplineDTO);
        when(disciplineConnector.findByTeacherId(1L)).thenReturn(input);
        List<Discipline> result = disciplineService.findDisciplines();
        assertEquals(1, result.size());
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
}