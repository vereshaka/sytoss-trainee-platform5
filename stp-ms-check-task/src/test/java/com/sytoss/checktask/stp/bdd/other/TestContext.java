package com.sytoss.checktask.stp.bdd.other;


import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;

@Getter
@Setter
public class TestContext {

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

