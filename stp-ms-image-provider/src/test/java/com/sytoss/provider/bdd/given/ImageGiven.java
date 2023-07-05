package com.sytoss.provider.bdd.given;

import com.sytoss.provider.bdd.CucumberIntegrationTest;
import com.sytoss.provider.bdd.common.TestExecutionContext;
import com.sytoss.provider.dto.ImageDTO;

import io.cucumber.java.en.Given;

public class ImageGiven extends CucumberIntegrationTest {

    @Given("^generated question image \"(.*)\" with id (.*)")
    public void generatedQuestionImage(String question,String id) {
        Long idValue = 1L;
        byte[] photoBytes = {0x01, 0x02, 0x03};
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setImageBytes(photoBytes);
        imageDTO.setId(idValue);
        getImageConnector().save(imageDTO);
        TestExecutionContext.getTestContext().getIdMapping().put(id,idValue);
    }

    @Given("^question image \"(.*)\" with id (.*) doesnt exist")
    public void questionImageWithIdDoesntExist(String question,String id) {
        if(TestExecutionContext.getTestContext().getIdMapping().get(id) != null){
            ImageDTO imageDTO = getImageConnector().getReferenceById(TestExecutionContext.getTestContext().getIdMapping().get(id));
            getImageConnector().delete(imageDTO);
        }
    }
}
