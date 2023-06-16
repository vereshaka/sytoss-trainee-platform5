package com.sytoss.producer.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.producer.AbstractApplicationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public class AbstractControllerTest extends AbstractApplicationTest {

    @BeforeAll
    public static void setupJWK() throws JOSEException, IOException {
        AbstractApplicationTest.setupJWK();
    }

    @AfterAll
    public static void stopServer() {
        AbstractApplicationTest.stopServer();
    }
}
