package com.sytoss.checktask.stp.cucumber;


import bom.QueryResult;
import com.sytoss.checktask.stp.service.DatabaseHelperService;
import com.sytoss.checktask.stp.service.QueryResultConvertor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestContext {

    private final ThreadLocal<DatabaseHelperService> databaseHelperService =
            ThreadLocal.withInitial(() -> new DatabaseHelperService(new QueryResultConvertor()));

    private QueryResult answer;

    private QueryResult etalon;

    private static final ThreadLocal<TestContext> testContext = new ThreadLocal<>();

    public static TestContext getInstance() {
        if (testContext.get() == null) {
            testContext.set(new TestContext());
        }
        return testContext.get();
    }
}

