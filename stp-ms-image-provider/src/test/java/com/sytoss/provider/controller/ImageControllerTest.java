package com.sytoss.provider.controller;

import com.sytoss.provider.exceptions.ConvertToImageException;
import com.sytoss.provider.exceptions.ImageNotFoundException;
import com.sytoss.provider.services.ImageService;
import com.sytoss.stp.test.StpApplicationTest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ImageControllerTest extends StpApplicationTest {

    @MockBean
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    @Test
    void convertImageWithError() {
        when(imageService.generatePngFromQuestion(any())).thenThrow(new ConvertToImageException("", new RuntimeException()));

        HttpHeaders  httpHeaders = getDefaultHttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("question", "anything");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body,  httpHeaders);

        ResponseEntity<String> responseEntity = doPost("/api/image/convert", requestEntity, String.class);

        Assertions.assertEquals(400, responseEntity.getStatusCode().value());
    }

    @Test
    void convertImageWithoutError() {
        HttpHeaders  httpHeaders = getDefaultHttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("question", "anything");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body,  httpHeaders);

        ResponseEntity<String> responseEntity = doPost("/api/image/convert", requestEntity, String.class);

        Assertions.assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void shouldGetImageById() {
        byte[] photoBytes = new byte[]{};
        when(imageService.getById(any())).thenReturn(photoBytes);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> result = doGet("/api/image/question/5", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldGetImageByIdWhichDoesntExist() {
        when(imageService.getById(any())).thenThrow(new ImageNotFoundException("123", new EntityNotFoundException()));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> result = doGet("/api/image/question/5", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(404, result.getStatusCode().value());
    }
}