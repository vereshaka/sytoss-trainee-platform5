package com.sytoss.producer.bdd.common;

import com.nimbusds.jose.JOSEException;
import com.sytoss.producer.AbstractApplicationTest;
import com.sytoss.producer.bdd.CucumberIntegrationTest;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import java.io.IOException;

public class STPHooks extends CucumberIntegrationTest {

    @BeforeAll
    public static void setupJWK() throws JOSEException, IOException {
        AbstractApplicationTest.setupJWK();
    }

    @AfterAll
    public static void stopServer() {
        AbstractApplicationTest.stopServer();
    }

    @After
    public void tearDown() {
        IntegrationTest.drop();
    }
}
