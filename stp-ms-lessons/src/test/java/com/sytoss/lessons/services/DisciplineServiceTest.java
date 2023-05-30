package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DisciplineServiceTest extends AbstractJunitTest {
//AbstractApplicationTest
    @Mock
    private DisciplineConnector disciplineConnector;

    @InjectMocks
    @Autowired
    private DisciplineService disciplineService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private DisciplineConvertor disciplineConvertor;

    @Test
    public void shouldSaveDiscipline() {
        when(teacherService.getById(1L)).thenReturn(createReference(1L));
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
        when(disciplineConnector.getReferenceById(any())).thenReturn(input);
        Discipline result = disciplineService.getById(1L);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getName(), result.getName());
    }

    @Test
    public void shouldRaiseExceptionWhenDisciplineNotExist() {
        when(disciplineConnector.getReferenceById(1L)).thenReturn(null);
        assertThrows(DisciplineNotFoundException.class, () -> disciplineService.getById(1L));
    }

}

//import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
//import com.sytoss.domain.bom.lessons.Discipline;
//import com.sytoss.lessons.AbstractApplicationTest;
//import com.sytoss.lessons.connectors.DisciplineConnector;
//import com.sytoss.lessons.dto.DisciplineDTO;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//public class DisciplineServiceTest extends AbstractApplicationTest {
//
//    @MockBean
//    private DisciplineConnector disciplineConnector;
//
//    @InjectMocks
//    @Autowired
//    private DisciplineService disciplineService;
//
//    @Test
//    public void getDisciplineById() {
//        DisciplineDTO input = new DisciplineDTO();
//        input.setId(1L);
//        input.setName("SQL");
//        when(disciplineConnector.getReferenceById(any())).thenReturn(input);
//        Discipline result = disciplineService.getById(1L);
//        assertEquals(input.getId(), result.getId());
//        assertEquals(input.getName(), result.getName());
//    }
//
//    @Test
//    public void shouldRaiseExceptionWhenDisciplineNotExist() {
//        when(disciplineConnector.getReferenceById(1L)).thenReturn(null);
//        assertThrows(DisciplineNotFoundException.class, () -> disciplineService.getById(1L));
//    }
//}