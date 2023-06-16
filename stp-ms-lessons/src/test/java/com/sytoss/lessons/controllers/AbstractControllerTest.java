package com.sytoss.lessons.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.connectors.UserConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

public class AbstractControllerTest extends AbstractApplicationTest {

    @MockBean
    private UserConnector userConnector;

    @BeforeAll
    public static void setupJWK() throws JOSEException, IOException {
        AbstractApplicationTest.setupJWK();
    }

    @AfterAll
    public static void stopServer() {
        AbstractApplicationTest.stopServer();
    }
}
