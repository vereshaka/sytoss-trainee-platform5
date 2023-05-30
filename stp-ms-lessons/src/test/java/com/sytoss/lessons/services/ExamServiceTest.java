package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.dto.ExamDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class ExamServiceTest extends AbstractApplicationTest {

    @InjectMocks
    @Autowired
    private ExamService examService;

    @Mock
    private ExamConnector examConnector;


    @Test
    public void shouldSaveExam() {
        Mockito.doAnswer((org.mockito.stubbing.Answer<ExamDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            ExamDTO result = (ExamDTO) args[0];
            result.setId(1L);
            return result;
        }).when(examConnector).save(any());

        Exam exam = new Exam();
        exam.setName("Exam");
        exam.setRelevantFrom(new Date());
        exam.setRelevantTo(new Date());
        exam.setGroup(new Group());
        exam.setDuration(15);
        exam.setNumberOfTasks(10);
        exam.setTopics(List.of(new Topic(), new Topic()));

        Exam result = examService.save(exam);
        Assertions.assertEquals(1L, result.getId());
    }
}
