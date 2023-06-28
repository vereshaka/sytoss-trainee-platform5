package com.sytoss.provider.bdd.given;

import com.sytoss.provider.bdd.CucumberIntegrationTest;
import com.sytoss.provider.dto.ImageDTO;
import io.cucumber.java.en.Given;

public class ImageGiven extends CucumberIntegrationTest {

    @Given("^generated question image \"(.*)\"")
    public void generatedQuestionImage(String question) {
        byte[] photoBytes = {0x01, 0x02, 0x03};
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setImageBytes(photoBytes);
        imageDTO.setId(1L);
        getImageConnector().save(imageDTO);
    }
}
