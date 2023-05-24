package com.sytoss.lessons.bdd.common;

public class IntegrationTest {

    private static final ThreadLocal<TestContext> testContext = new ThreadLocal<>();

    public static TestContext getTestContext() {
        if (testContext.get() == null) {
            testContext.set(new TestContext());
        }
        return testContext.get();
    }

    public static void drop() {
        testContext.set(null);
    }
}
