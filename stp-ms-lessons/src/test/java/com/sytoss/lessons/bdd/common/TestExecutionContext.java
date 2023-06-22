package com.sytoss.lessons.bdd.common;

import com.sytoss.domain.bom.personalexam.PersonalExam;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TestExecutionContext {

    private static final ThreadLocal<TestExecutionContext> testContext = new ThreadLocal<>();

    private ResponseEntity response;

    private Long disciplineId;

    private Long topicId;

    private Long teacherId;

    private Long taskId;

    private Long taskDomainId;

    private Long taskConditionId;

    private Long groupReferenceId;

    private String token;

    private List<PersonalExam> personalExams = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    private Map<String, Long> idMapping = new HashMap<>();

    public static TestExecutionContext getTestContext() {
        if (testContext.get() == null) {
            testContext.set(new TestExecutionContext());
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
}
