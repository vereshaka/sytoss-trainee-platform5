package com.sytoss.users.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.users.AbstractApplicationTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.ArrayList;

public class AbstractControllerTest extends AbstractApplicationTest {

    @BeforeAll
    public static void setupJWK() throws JOSEException, IOException {
        AbstractApplicationTest.setupJWK();
    }

    @AfterAll
    public static void stopServer() {
        AbstractApplicationTest.stopServer();
    }

    protected HttpHeaders getDefaultHttpHeaders() {
        HttpHeaders result = new HttpHeaders();
        if (StringUtils.isNoneEmpty()) {
            result.setBearerAuth(getToken());
        }
        return result;
    }

    protected String getToken() {
        return generateJWT(new ArrayList<>(), "John", "Johnson", "test@test.com", "teacher");
    }
}
