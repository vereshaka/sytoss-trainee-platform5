package com.sytoss.lessons.bdd.common;

import com.nimbusds.jose.JOSEException;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import java.io.IOException;

@Deprecated
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
