package com.sytoss.provider.services;

import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest extends StpUnitTest {

    private final ImageService imageService = new ImageService();

    @Test
    void generatePngFromQuestion() {
    }

    @Test
    void convertToImage() {
        imageService.generatePngFromQuestion("Anything");
    }
}