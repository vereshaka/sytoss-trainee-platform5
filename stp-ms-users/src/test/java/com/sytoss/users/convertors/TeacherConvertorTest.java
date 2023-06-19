package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.dto.TeacherDTO;
import com.sytoss.users.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeacherConvertorTest extends AbstractJunitTest {

    @InjectMocks
    private TeacherConvertor teacherConvertor;

    @Test
    public void toDTOTeacherConvertorTest() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Luidji");
        teacher.setLastName("Monk");
        teacher.setEmail("test@email.com");
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherConvertor.toDTO(teacher, teacherDTO);
        assertEquals(teacher.getId(), teacherDTO.getId());
        assertEquals(teacher.getFirstName(), teacherDTO.getFirstName());
        assertEquals(teacher.getLastName(), teacherDTO.getLastName());
        assertEquals(teacher.getEmail(), teacherDTO.getEmail());
    }

    @Test
    public void fromDTOTeacherConvertorTest() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setFirstName("Luidji");
        teacherDTO.setLastName("Monk");
        teacherDTO.setEmail("test@email.com");
        Teacher teacher = new Teacher();
        teacherConvertor.fromDTO(teacherDTO, teacher);
        assertEquals(teacher.getId(), teacherDTO.getId());
        assertEquals(teacher.getFirstName(), teacherDTO.getFirstName());
        assertEquals(teacher.getLastName(), teacherDTO.getLastName());
        assertEquals(teacher.getEmail(), teacherDTO.getEmail());
    }
}
