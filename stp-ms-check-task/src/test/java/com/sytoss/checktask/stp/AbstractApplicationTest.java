package com.sytoss.checktask.stp;

import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractApplicationTest extends StpUnitTest {

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

    protected <T> ResponseEntity<T> perform(String uri, HttpMethod method, HttpEntity requestEntity, Class<T> responseType) {
        return restTemplate.exchange(getEndpoint(uri), method, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doPost(String uri, HttpEntity requestEntity, Class<T> responseType) {
        return perform(uri, HttpMethod.POST, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> doGet(String uri, HttpEntity requestEntity, Class<T> responseType) {
        return perform(uri, HttpMethod.GET, requestEntity, responseType);
    }
}