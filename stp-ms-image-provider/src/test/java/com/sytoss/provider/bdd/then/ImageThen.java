package com.sytoss.provider.bdd.then;

import com.sytoss.provider.bdd.ImageProviderIntegrationTest;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ImageThen extends ImageProviderIntegrationTest {

    @Then("operation is successful")
    public void createImageOfTheQuestion() {
        log.info("Body = [{}]",getTestExecutionContext().getResponse().getBody());
        assertEquals(200, getTestExecutionContext().getResponse().getStatusCode().value());
    }

    @Then("image should be retrieved")
    public void imageIdShouldBe() {
        byte[] photoBytes = (byte[]) getTestExecutionContext().getResponse().getBody();
        Assertions.assertNotNull(photoBytes);
    }

    @Then("^operation should be finished with (\\w+) \"(.*)\" error$")
    public void raiseError(Integer status, String error) {
        assertEquals(status, getTestExecutionContext().getResponse().getStatusCode().value());
    }
}
