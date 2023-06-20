package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentConvertorTest extends AbstractJunitTest {

    @Spy
    private StudentConverter studentConverter = new StudentConverter(new GroupConvertor());

    @Test
    public void toDTOStudentConvertorTest() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Firstname");
        student.setLastName("Lastname");
        student.setModerated(false);
        student.setEmail("test@gmail.com");
        Group group = new Group();
        group.setId(1L);
        student.setGroup(group);
        byte[] photoBytes = {1, 2, 3, 4, 5};
        student.setPhoto(photoBytes);
        StudentDTO studentDTO = new StudentDTO();
        studentConverter.toDTO(student, studentDTO);
        assertEquals(student.getId(), studentDTO.getId());
        assertEquals(student.getFirstName(), studentDTO.getFirstName());
        assertEquals(student.getLastName(), studentDTO.getLastName());
        assertEquals(student.getEmail(), studentDTO.getEmail());
        assertEquals(student.isModerated(), studentDTO.isModerated());
        assertEquals(student.getPhoto(), studentDTO.getPhoto());
    }

    @Test
    public void fromDTOStudentConvertorTest() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFirstName("Firstname");
        studentDTO.setLastName("Lastname");
        studentDTO.setModerated(false);
        studentDTO.setEmail("test@email.com");
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        studentDTO.setGroup(groupDTO);
        byte[] photoBytes = {1, 2, 3, 4, 5};
        studentDTO.setPhoto(photoBytes);
        Student student = new Student();
        studentConverter.fromDTO(studentDTO, student);
        assertEquals(student.getId(), studentDTO.getId());
        assertEquals(student.getFirstName(), studentDTO.getFirstName());
        assertEquals(student.getLastName(), studentDTO.getLastName());
        assertEquals(student.isModerated(), studentDTO.isModerated());
        assertEquals(student.getEmail(), studentDTO.getEmail());
    }
}
