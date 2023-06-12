package com.sytoss.users.controllers;

import com.sytoss.users.AbstractApplicationTest;
import com.sytoss.users.services.StudentService;
import com.sytoss.users.util.UpdatePhotoRequestParams;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StudentControllerTest extends AbstractApplicationTest {

    @InjectMocks
    private StudentController studentController;

    @MockBean
    private StudentService studentService;

    @Test
    public void shouldUpdatePhoto() {

//        MultipartFile photo = mock(MultipartFile.class);

        byte[] photoBytes = { 0x01, 0x02, 0x03 };
        MultipartFile photo = new MockMultipartFile("photo.jpg", photoBytes);

        when(studentService.updatePhoto(any(), any())).thenReturn(photo);

        HttpHeaders headers = new HttpHeaders();
        UpdatePhotoRequestParams requestParams = new UpdatePhotoRequestParams();
        requestParams.setEmail("test email");
        requestParams.setPhoto(photo);
        HttpEntity<UpdatePhotoRequestParams> requestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<MultipartFile> result = doPost("/api/student/updatePhoto", requestEntity, new ParameterizedTypeReference<MultipartFile>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }
}
