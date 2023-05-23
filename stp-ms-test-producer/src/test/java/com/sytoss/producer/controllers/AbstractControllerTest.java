package com.sytoss.producer.controllers;

import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class AbstractControllerTest extends AbstractSTPProducerApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, Object requestEntity, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (requestEntity instanceof HttpEntity && ((HttpEntity) requestEntity).getHeaders() != null) {
            for (Map.Entry entry : ((HttpEntity) requestEntity).getHeaders().entrySet()) {
                headers.addAll(entry.getKey().toString(), (List<? extends String>) entry.getValue());
            }
        }
        HttpEntity request = new HttpEntity<>(requestEntity, headers);
        return restTemplate.exchange(getEndpoint(uri), method, request, responseType);
    }

    public <T> ResponseEntity<T> doPost(String uri, Object requestEntity, Class<T> responseType) {
        return perform(uri, HttpMethod.POST, requestEntity, responseType);
    }
}
