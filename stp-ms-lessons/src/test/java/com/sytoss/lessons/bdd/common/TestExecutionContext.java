package com.sytoss.lessons.bdd.common;

import com.sytoss.domain.bom.users.Teacher;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.List;

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

    private Long disciplineId;

    private Long topicId;

    private Long teacherId;

    private Long taskId;

    private Long taskDomainId;

    List<Teacher> teacherList;
}
