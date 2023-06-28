package com.sytoss.provider.services;

import com.sytoss.provider.connector.ImageConnector;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class ImageServiceTest extends StpUnitTest {

    private final ImageService imageService = new ImageService(mock(ImageConnector.class));

    @Test
    void generatePngFromQuestion() {
    }

    @Test
    void convertToImage() {
        Long id = imageService.generatePngFromQuestion("Anything");
        Assertions.assertNotNull(id);
    }
}