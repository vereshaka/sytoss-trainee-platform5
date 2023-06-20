package com.sytoss.users.bdd.common;

import com.sytoss.users.dto.UserDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

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

    @Setter(AccessLevel.NONE)
    private Map<String, Long> idMapping = new HashMap<>();

    public void registerId(String key, Long id) {
        if (idMapping.containsKey(key)) {
            throw new IllegalArgumentException("Key already registered. Key: " + key);
        }
        idMapping.put(key, id);
    }
}
