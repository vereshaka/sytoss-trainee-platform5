package com.sytoss.provider.bdd.then;

import com.sytoss.provider.bdd.CucumberIntegrationTest;
import com.sytoss.provider.bdd.common.TestExecutionContext;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ImageThen extends CucumberIntegrationTest {

    @Then("operation is successful")
    public void createImageOfTheQuestion() {
        log.info("Body = [{}]", TestExecutionContext.getTestContext().getResponse().getBody());
        assertEquals(200, TestExecutionContext.getTestContext().getResponse().getStatusCode().value());
    }

    @Then("image should be retrieved")
    public void imageIdShouldBe() {
        byte[] photoBytes = (byte[]) TestExecutionContext.getTestContext().getResponse().getBody();
        Assertions.assertNotNull(photoBytes);
    }

    @Then("^operation should be finished with (\\w+) error$")
    public void raiseError(Integer status, String error) {
        assertEquals(status, TestExecutionContext.getTestContext().getResponse().getStatusCode().value());
        assertArrayEquals(error.getBytes(), (byte[]) TestExecutionContext.getTestContext().getResponse().getBody());
    }
}
