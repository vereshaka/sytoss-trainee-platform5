package com.sytoss.users.services;

import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.connectors.StudentConnector;
import com.sytoss.users.dto.StudentDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest extends AbstractJunitTest {

    @Mock
    private StudentConnector studentConnector;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testUpdatePhoto() {
        String email = "test@example.com";
        byte[] photoBytes = { 0x01, 0x02, 0x03 };
        MultipartFile photo = new MockMultipartFile("photo.jpg", photoBytes);

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setEmail(email);

        when(studentConnector.getByEmail(email)).thenReturn(studentDTO);
        when(studentConnector.save(studentDTO)).thenReturn(studentDTO);

        MultipartFile result = studentService.updatePhoto(email, photo);

        assertArrayEquals(photoBytes, studentDTO.getPhoto());

        verify(studentConnector).save(studentDTO);

        assertFalse(studentDTO.isModerated());
        assertEquals(photo, result);
    }
}
