package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.lessons.AbstractLessonsApplicationTest;
import com.sytoss.lessons.connectors.ExamConnector;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ExamServiceTest extends AbstractLessonsApplicationTest {

    @InjectMocks
    private ExamService examService;

    @MockBean
    private ExamConnector examConnector;

    @Test
    public void shouldSaveExam() {
        examService.save(new Exam());
        verify(examConnector.save(any()));
    }
}
