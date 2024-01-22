package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.PersonalExamIntegrationException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PersonalExamServiceTest extends StpUnitTest {

    @Mock
    private PersonalExamConnector personalExamConnector;

    @Mock
    private ExecutorService executor;

    @InjectMocks
    private PersonalExamService personalExamService;

    @Test
    public void shouldFailOnPersonalExamCreation() {
        List<ExamConfiguration> configurations = List.of(createExamConfiguration(1L),
                createExamConfiguration(2L));

        when(executor.submit(any(Callable.class))).thenReturn(
                CompletableFuture.completedFuture("error message"),
                CompletableFuture.completedFuture(null));

        PersonalExamIntegrationException exception = assertThrows(PersonalExamIntegrationException.class,
                () -> personalExamService.createPersonalExams(configurations));
        assertEquals("error message", exception.getMessage());
    }

    private ExamConfiguration createExamConfiguration(Long studentId) {
        ExamConfiguration examConfiguration = new ExamConfiguration();
        Exam exam = new Exam();
        exam.setId(1L);

        ExamAssignee examAssignee = new ExamAssignee();
        examAssignee.setExam(exam);

        examConfiguration.setExam(exam);
        examConfiguration.setExamAssignee(examAssignee);
        examConfiguration.setStudent(createStudent(1L));

        return examConfiguration;
    }


    private Student createStudent(Long id) {
        Student student = new Student();
        student.setId(id);
        student.setUid("teststs" + id);

        return student;
    }
}
