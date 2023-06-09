package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentConvertorTest extends AbstractJunitTest {

    @InjectMocks
    private StudentConvertor studentConvertor;

    @Test
    public void toDTOStudentConvertorTest() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Firstname");
        student.setLastName("Lastname");
        student.setMiddleName("Middlename");
        student.setEmail("test@gmail.com");
        student.setPhoto("test photo");
        StudentDTO studentDTO = new StudentDTO();
        studentConvertor.toDTO(student, studentDTO);
        assertEquals(student.getId(), studentDTO.getId());
        assertEquals(student.getFirstName(), studentDTO.getFirstName());
        assertEquals(student.getMiddleName(), studentDTO.getMiddleName());
        assertEquals(student.getLastName(), studentDTO.getLastName());
        assertEquals(student.getEmail(), studentDTO.getEmail());
        assertEquals(student.getPhoto(), studentDTO.getPhoto());
    }

    @Test
    public void fromDTOTeacherConvertorTest() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFirstName("Firstname");
        studentDTO.setLastName("Lastname");
        studentDTO.setMiddleName("Middlename");
        studentDTO.setEmail("test@email.com");
        studentDTO.setPhoto("test photo");
        Student student = new Student();
        studentConvertor.fromDTO(studentDTO, student);
        assertEquals(student.getId(), studentDTO.getId());
        assertEquals(student.getFirstName(), studentDTO.getFirstName());
        assertEquals(student.getMiddleName(), studentDTO.getMiddleName());
        assertEquals(student.getLastName(), studentDTO.getLastName());
        assertEquals(student.getEmail(), studentDTO.getEmail());
    }
}
