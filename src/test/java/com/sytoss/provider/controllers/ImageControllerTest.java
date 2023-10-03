package com.sytoss.provider.controllers;

import com.sytoss.provider.services.ImageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ImageControllerTest extends AbstractControllerTest {

    @InjectMocks
    private ImageController imageController;

    @MockBean
    private ImageService imageService;

    @Test
    public void shouldSaveImage() throws IOException {
        when(imageService.saveImage(any())).thenReturn("");
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(new byte[100], headers);
        ResponseEntity<String> result = doPost("/api/save-image", requestEntity, String.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldGetImageByName() throws IOException {
        when(imageService.getImageByName(anyString())).thenReturn(new byte[]{});
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = doGet("/public/api/image/1.jpg", requestEntity, String.class);
        assertEquals(200, result.getStatusCode().value());
    }
}
