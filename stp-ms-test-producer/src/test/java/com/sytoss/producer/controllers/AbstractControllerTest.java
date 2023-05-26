package com.sytoss.producer.controllers;

import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class AbstractControllerTest extends AbstractSTPProducerApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, HttpEntity requestEntity, Class<T> responseType) {
        return restTemplate.exchange(getEndpoint(uri), method, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doGet(String uri, HttpEntity requestEntity, Class<T> responseType) {
        return perform(uri, HttpMethod.GET, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doPost(String uri, HttpEntity requestEntity, Class<T> responseType) {
        return perform(uri, HttpMethod.POST, requestEntity, responseType);
    }
}
