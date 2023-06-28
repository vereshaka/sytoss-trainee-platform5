package com.sytoss.provider.services;

import com.sytoss.provider.connector.ImageConnector;
import com.sytoss.provider.dto.ImageDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ImageServiceTest extends StpUnitTest {

    @Mock
    private ImageConnector imageConnector;
    @InjectMocks
    private ImageService imageService;

    @Test
    void generatePngFromQuestion() {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(1L);
        when(imageConnector.save(any())).thenReturn(imageDTO);
        Long id = imageService.generatePngFromQuestion("Anything");
        Assertions.assertNotNull(id);
    }
}