package com.sytoss.provider.bdd.when;

import com.sytoss.provider.bdd.ImageProviderIntegrationTest;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class ImageWhen extends ImageProviderIntegrationTest {

    @When("^retrieve image by id (.*)")
    public void retrieveImageById(String id) {
        Long idValue = (Long) getTestExecutionContext().getIdMapping().get(id);
        String url = "/api/image/question/" + idValue;
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<byte[]> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<byte[]> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }
}
