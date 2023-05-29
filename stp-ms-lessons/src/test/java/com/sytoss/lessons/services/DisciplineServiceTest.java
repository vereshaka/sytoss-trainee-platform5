package com.sytoss.lessons.services;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DisciplineServiceTest extends AbstractJunitTest {

    @Mock
    private DisciplineConnector disciplineConnector;

    @InjectMocks
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

}
