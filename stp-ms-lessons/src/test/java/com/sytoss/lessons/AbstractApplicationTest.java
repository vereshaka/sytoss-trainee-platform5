package com.sytoss.lessons;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
@Getter
public abstract class AbstractApplicationTest extends AbstractJunitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AbstractApplicationContext applicationContext;

    @Test
    public void shouldLoadApplicationContext() {
        assertNotNull(applicationContext);
    }

    protected long getPort() {
        return ((AnnotationConfigServletWebServerApplicationContext) applicationContext).getWebServer().getPort();
    }

    protected URI getEndpoint(String uriPath) {
        try {
            return new URI("http://localhost:" + getPort() + uriPath);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, Object requestEntity, ParameterizedTypeReference responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (requestEntity instanceof HttpEntity && ((HttpEntity) requestEntity).getHeaders() != null) {
            for (Map.Entry entry : ((HttpEntity) requestEntity).getHeaders().entrySet()) {
                headers.addAll(entry.getKey().toString(), (List<? extends String>) entry.getValue());
            }
        }
        HttpEntity request = new HttpEntity<>(requestEntity, headers);
        return restTemplate.exchange(getEndpoint(uri), method, request, responseType);
    }

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, Object requestEntity, Class responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (requestEntity instanceof HttpEntity && ((HttpEntity) requestEntity).getHeaders() != null) {
            for (Map.Entry entry : ((HttpEntity) requestEntity).getHeaders().entrySet()) {
                headers.addAll(entry.getKey().toString(), (List<? extends String>) entry.getValue());
            }
        }
        HttpEntity request = new HttpEntity<>(requestEntity, headers);
        return restTemplate.exchange(getEndpoint(uri), method, request, responseType);
    }

    public <T> ResponseEntity<T> doPost(String uri, Object requestEntity, ParameterizedTypeReference responseType) {
        return perform(uri, HttpMethod.POST, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doGet(String uri, Object requestEntity, ParameterizedTypeReference responseType) {
        return perform(uri, HttpMethod.GET, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doPatch(String uri, Object requestEntity, ParameterizedTypeReference responseType) {
        return perform(uri, HttpMethod.PATCH, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doPost(String uri, Object requestEntity, Class responseType) {
        return perform(uri, HttpMethod.POST, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doPatch(String uri, Object requestEntity, Class responseType) {
        return perform(uri, HttpMethod.PATCH, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doGet(String uri, Object requestEntity, Class responseType) {
        return perform(uri, HttpMethod.GET, requestEntity, responseType);
    }
}
