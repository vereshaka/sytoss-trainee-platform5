package com.sytoss.lessons;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class})
public abstract class AbstractLessonsApplicationTest {

    @Autowired
    private AbstractApplicationContext applicationContext;

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
}
