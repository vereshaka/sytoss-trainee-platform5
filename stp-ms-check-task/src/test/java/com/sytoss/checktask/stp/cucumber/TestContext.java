package com.sytoss.checktask.stp.cucumber;


import bom.QueryResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestContext {

    private QueryResult answer;

    private QueryResult etalon;

    private static final ThreadLocal<TestContext> testContext = new ThreadLocal<>();

    public static TestContext getInstance() {
        if (testContext.get() == null) {
            testContext.set(new TestContext());
        }
        return testContext.get();
    }

    public static void dropInstance() {
        testContext.set(null);
    }
}

