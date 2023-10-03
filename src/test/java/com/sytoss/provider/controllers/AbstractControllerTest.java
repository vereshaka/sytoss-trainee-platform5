package com.sytoss.provider.controllers;

import com.nimbusds.jose.JOSEException;
import com.sytoss.provider.ImageProviderApplicationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public abstract class AbstractControllerTest extends ImageProviderApplicationTest {

    @BeforeAll
    public static void setupJWK() throws JOSEException, IOException {
        ImageProviderApplicationTest.setupJWK();
    }

    @AfterAll
    public static void stopServer() {
        ImageProviderApplicationTest.stopServer();
    }
}
