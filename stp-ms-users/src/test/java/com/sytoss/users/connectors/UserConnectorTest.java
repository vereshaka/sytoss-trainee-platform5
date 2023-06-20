package com.sytoss.users.connectors;

import com.sytoss.users.AbstractApplicationTest;
import com.sytoss.users.dto.StudentDTO;
import com.sytoss.users.dto.TeacherDTO;
import com.sytoss.users.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UserConnectorTest extends AbstractApplicationTest {

    @Autowired
    private UserConnector userConnector;

    @Test
    public void shouldSaveStudentDTO() {
        StudentDTO input = new StudentDTO();
        input.setEmail("student@domain.com");
        input.setFirstName("FirstName");
        input.setLastName("LastName");
        input.setPrimaryGroupId(1L);
        UserDTO result = userConnector.save(input);
        assertNotNull(result.getId());
        assertEquals(StudentDTO.class, result.getClass());
    }

    @Test
    public void shouldSaveTeacherDTO() {
        TeacherDTO input = new TeacherDTO();
        input.setEmail("teacher@domain.com");
        input.setFirstName("FirstName");
        input.setLastName("LastName");
        UserDTO result = userConnector.save(input);
        assertNotNull(result.getId());
        assertEquals(TeacherDTO.class, result.getClass());
    }

    @Test
    public void shouldReturnTeacherDTOById() {
        TeacherDTO input = new TeacherDTO();
        input.setEmail("teacher@domain.com");
        input.setFirstName("FirstName");
        input.setLastName("LastName");

        UserDTO result = userConnector.save(input);

        Long id = result.getId();
        result = userConnector.findById(id).get();
        assertEquals(id, result.getId());
        assertTrue(result instanceof TeacherDTO);
    }

    @Test
    public void shouldReturnList() {
        TeacherDTO tInput = new TeacherDTO();
        tInput.setEmail("teacher@domain.com");
        tInput.setFirstName("FirstName");
        tInput.setLastName("LastName");

        userConnector.save(tInput);

        StudentDTO sInput = new StudentDTO();
        sInput.setEmail("student@domain.com");
        sInput.setFirstName("FirstName");
        sInput.setLastName("LastName");
        sInput.setPrimaryGroupId(1L);
        userConnector.save(sInput);

        List<UserDTO> result = userConnector.findAll();
        boolean containsTeacher = result.stream().anyMatch(user -> user instanceof TeacherDTO);
        boolean containsStudent = result.stream().anyMatch(user -> user instanceof StudentDTO);
        assertTrue(containsTeacher);
        assertTrue(containsStudent);
    }
}
