package com.sytoss.users.services;

import com.sytoss.users.AbstractJunitTest;
import com.sytoss.users.connectors.StudentConnector;
import com.sytoss.users.dto.StudentDTO;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest extends AbstractJunitTest {

    @Mock
    private StudentConnector studentConnector;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testUpdatePhoto() {
        byte[] photoBytes = { 0x01, 0x02, 0x03 };
        MultipartFile photo = new MockMultipartFile("photo.jpg", photoBytes);
        studentService.updatePhoto(photo);
        ArgumentCaptor<StudentDTO> studentDTOCaptor = ArgumentCaptor.forClass(StudentDTO.class);
        verify(studentConnector).save(studentDTOCaptor.capture());
        StudentDTO savedStudentDTO = studentDTOCaptor.getValue();
        assertArrayEquals(photoBytes, savedStudentDTO.getPhoto());
        assertFalse(savedStudentDTO.isModerated());
    }
}