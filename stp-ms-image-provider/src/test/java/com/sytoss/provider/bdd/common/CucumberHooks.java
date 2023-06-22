package com.sytoss.provider.bdd.common;


import com.nimbusds.jose.JOSEException;
import com.sytoss.provider.bdd.CucumberIntegrationTest;
import com.sytoss.provider.AbstractApplicationTest;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import java.io.IOException;

public class CucumberHooks extends CucumberIntegrationTest {

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
        TestExecutionContext.drop();
    }
}
