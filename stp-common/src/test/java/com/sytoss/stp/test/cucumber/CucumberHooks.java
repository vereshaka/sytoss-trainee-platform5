package com.sytoss.stp.test.cucumber;

import com.sytoss.stp.test.StpApplicationTest;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;


public class CucumberHooks extends StpApplicationTest {

    @BeforeAll
    public static void initServer() {
        init();
    }

    @After
    public void testFinish() {
        TestExecutionContext.drop();
    }
}
