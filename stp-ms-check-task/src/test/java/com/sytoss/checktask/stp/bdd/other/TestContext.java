package com.sytoss.checktask.stp.bdd.other;


import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.checktask.stp.service.DatabaseHelperService;
import com.sytoss.domain.bom.personalexam.Score;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;

@Getter
@Setter
public class TestContext {

    private final ObjectProvider<DatabaseHelperService> databaseHelperServiceProvider = mock(ObjectProvider.class);

    private CheckTaskParameters checkTaskParameters = new CheckTaskParameters();

    private ResponseEntity<String> responseEntity;

    private Score score;

    private static final ThreadLocal<TestContext> testContext = new ThreadLocal<>();

    public static TestContext getInstance() {
        if (testContext.get() == null) {
            testContext.set(new TestContext());
        }
        return testContext.get();
    }

    public static void drop() {
        testContext.set(null);
    }
}

