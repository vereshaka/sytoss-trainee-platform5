package com.sytoss.users.services;

import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.connectors.StudentConnector;
import com.sytoss.users.dto.StudentDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest extends AbstractJunitTest {

    @Mock
    private StudentConnector studentConnector;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testUpdatePhoto() throws IOException {
        String email = "test@example.com";
        MultipartFile photo = createMockMultipartFile();

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setEmail(email);

        when(studentConnector.getByEmail(email)).thenReturn(studentDTO);

        studentService.updatePhoto(email, photo);

        verify(studentConnector).getByEmail(email);
        verify(studentConnector).save(studentDTO);

        assertFalse(studentDTO.isModerated());
        assertEquals(getBase64EncodedString(photo.getBytes()), studentDTO.getPhoto());
        assertEquals(photo, studentService.updatePhoto(email, photo));
    }

    private MultipartFile createMockMultipartFile() {
        byte[] data = "Test Photo".getBytes();
        return new MockMultipartFile("test.jpg", data);
    }

    private String getBase64EncodedString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
