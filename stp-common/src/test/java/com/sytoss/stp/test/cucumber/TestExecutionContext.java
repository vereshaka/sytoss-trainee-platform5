package com.sytoss.stp.test.cucumber;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TestExecutionContext<T> {

    private static final ThreadLocal<TestExecutionContext> testContext = new ThreadLocal<>();
    private ResponseEntity response;
    private String token;
    private T details;
    @Setter(AccessLevel.NONE)
    private Map<String, Long> idMapping = new HashMap<>();

    public static <T> TestExecutionContext<T> getTestContext() {
        if (testContext.get() == null) {
            testContext.set(new TestExecutionContext<T>());
        }
        return testContext.get();
    }

    public static void drop() {
        testContext.set(null);
    }

    public void registerId(String key, Long id) {
        if (idMapping.containsKey(key)) {
            throw new IllegalArgumentException("Key already registered. Key: " + key);
        }
        idMapping.put(key, id);
    }

    public Long replaceId(String key) {
       Long result = idMapping.get(key);
       if (result == null) {
           result = Long.valueOf(key);
       }
       return result;
    }
}
