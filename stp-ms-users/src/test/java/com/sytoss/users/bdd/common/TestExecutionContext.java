package com.sytoss.users.bdd.common;

import com.sytoss.users.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class TestExecutionContext {

    private static final ThreadLocal<TestExecutionContext> testContext = new ThreadLocal<>();

    public static TestExecutionContext getTestContext() {
        if (testContext.get() == null) {
            testContext.set(new TestExecutionContext());
        }
        return testContext.get();
    }

    public static void drop() {
        testContext.set(null);
    }

    private ResponseEntity response;

    private UserDTO user;
}
