package com.sytoss.provider.controller;

import com.sytoss.provider.exceptions.ConvertToImageException;
import com.sytoss.provider.services.ImageService;
import com.sytoss.stp.test.StpApplicationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

        HttpHeaders headers = getDefaultHttpHeaders();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("question", "anything");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = doPost("/api/convert/image", requestEntity, String.class);

        Assertions.assertEquals(400, responseEntity.getStatusCode().value());
    }
}