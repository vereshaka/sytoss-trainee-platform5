package com.sytoss.users.services;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.connectors.TeacherConnector;
import com.sytoss.users.convertors.TeacherConvertor;
import com.sytoss.users.dto.TeacherDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class TeacherServiceTest extends AbstractJunitTest {

    @Mock
    private TeacherConnector teacherConnector;

    @InjectMocks
    @Autowired
    private TeacherService teacherService;

    @Spy
    private TeacherConvertor teacherConvertor = new TeacherConvertor();

    @Test
    @Disabled
    public void shouldSaveTeacher() {
        Mockito.doAnswer((Answer<TeacherDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            TeacherDTO result = (TeacherDTO) args[0];
            result.setId(1L);
            return result;
        }).when(teacherConnector).save(any(TeacherDTO.class));
        Teacher teacher = new Teacher();
        teacher.setFirstName("Luidji");
        teacher.setLastName("Monk");
        teacher.setMiddleName("Hoki");
        teacher.setEmail("test@email.com");
        Teacher result = null; //teacherService.create(teacher);
        assertEquals(1L, result.getId());
        assertEquals("Luidji", result.getFirstName());
        assertEquals("Monk", result.getLastName());
        assertEquals("Hoki", result.getMiddleName());
        assertEquals("test@email.com", result.getEmail());
    }
}