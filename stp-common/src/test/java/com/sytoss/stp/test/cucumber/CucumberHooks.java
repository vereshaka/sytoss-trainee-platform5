package com.sytoss.stp.test.cucumber;

import com.sytoss.stp.test.StpApplicationTest;
import io.cucumber.java.After;


public class CucumberHooks extends StpApplicationTest {

    @After
    public void testFinish() {
       TestExecutionContext.drop();
    }
}
