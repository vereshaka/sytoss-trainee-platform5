package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.stp.test.StpUnitTest;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserConvertorTest extends StpUnitTest {

    @InjectMocks
    private UserConverter userConverter;

    @Test
    public void toDTOStudentConvertorTest() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Firstname");
        student.setLastName("Lastname");
        student.setModerated(false);
        student.setEmail("test@gmail.com");
        student.setUid("thisIsNotLongId");
        student.setImageName("Image name");
        Group group = new Group();
        group.setId(1L);
        student.setPrimaryGroup(group);
        StudentDTO studentDTO = new StudentDTO();
        userConverter.toDTO(student, studentDTO);
        assertEquals(student.getId(), studentDTO.getId());
        assertEquals(student.getFirstName(), studentDTO.getFirstName());
        assertEquals(student.getLastName(), studentDTO.getLastName());
        assertEquals(student.getEmail(), studentDTO.getEmail());
        assertEquals(student.isModerated(), studentDTO.isModerated());
        assertEquals(student.getUid(), studentDTO.getUid());
        assertEquals(student.getImageName(), studentDTO.getImageName());
    }

    @Test
    public void fromDTOStudentConvertorTest() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFirstName("Firstname");
        studentDTO.setLastName("Lastname");
        studentDTO.setModerated(false);
        studentDTO.setEmail("test@email.com");
        studentDTO.setUid("thisIsNotLongId");
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        groupDTO.setStudents(List.of(new StudentDTO[]{studentDTO}));
        studentDTO.setPrimaryGroup(groupDTO);
        byte[] photoBytes = {1, 2, 3, 4, 5};
        studentDTO.setPhoto(photoBytes);
        Student student = new Student();
        userConverter.fromDTO(studentDTO, student);
        assertEquals(student.getId(), studentDTO.getId());
        assertEquals(student.getFirstName(), studentDTO.getFirstName());
        assertEquals(student.getLastName(), studentDTO.getLastName());
        assertEquals(student.isModerated(), studentDTO.isModerated());
        assertEquals(student.getEmail(), studentDTO.getEmail());
        assertEquals(student.getUid(), studentDTO.getUid());
    }

    @Test
    public void toDTOTeacherConvertorTest() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Luidji");
        teacher.setLastName("Monk");
        teacher.setEmail("test@email.com");
        teacher.setUid("thisIsNotLongId");
        TeacherDTO teacherDTO = new TeacherDTO();
        userConverter.toDTO(teacher, teacherDTO);
        assertEquals(teacher.getId(), teacherDTO.getId());
        assertEquals(teacher.getFirstName(), teacherDTO.getFirstName());
        assertEquals(teacher.getLastName(), teacherDTO.getLastName());
        assertEquals(teacher.getEmail(), teacherDTO.getEmail());
        assertEquals(teacher.getUid(), teacherDTO.getUid());
    }

    @Test
    public void fromDTOTeacherConvertorTest() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setFirstName("Luidji");
        teacherDTO.setLastName("Monk");
        teacherDTO.setEmail("test@email.com");
        teacherDTO.setUid("thisIsNotLongId");
        Teacher teacher = new Teacher();
        userConverter.fromDTO(teacherDTO, teacher);
        assertEquals(teacher.getId(), teacherDTO.getId());
        assertEquals(teacher.getFirstName(), teacherDTO.getFirstName());
        assertEquals(teacher.getLastName(), teacherDTO.getLastName());
        assertEquals(teacher.getEmail(), teacherDTO.getEmail());
        assertEquals(teacher.getUid(), teacherDTO.getUid());
    }
}
