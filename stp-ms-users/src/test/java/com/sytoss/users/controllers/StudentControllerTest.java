package com.sytoss.users.controllers;

import com.sytoss.users.AbstractApplicationTest;
import com.sytoss.users.services.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentControllerTest extends AbstractApplicationTest {

    @Autowired
    @InjectMocks
    private StudentController studentController;

    @MockBean
    private StudentService studentService;

    @Test
    public void shouldUpdatePhoto() {
        byte[] photoBytes = { 0x01, 0x02, 0x03 };
        File photoFile;
        try {
            photoFile = File.createTempFile("photo", ".jpg");
            Files.write(photoFile.toPath(), photoBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("photo", new FileSystemResource(photoFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Void> result = getRestTemplate().postForEntity(getEndpoint("/api/student/updatePhoto"), requestEntity, Void.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
