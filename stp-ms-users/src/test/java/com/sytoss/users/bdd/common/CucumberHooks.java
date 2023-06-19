package com.sytoss.users.bdd.common;


import com.nimbusds.jose.JOSEException;
import com.sytoss.users.AbstractApplicationTest;
import com.sytoss.users.bdd.CucumberIntegrationTest;
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
