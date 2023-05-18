package com.sytoss.producer.services;

import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import com.sytoss.producer.bom.*;
import com.sytoss.producer.connectors.PersonalExamConnector;
import com.sytoss.producer.exceptions.businessException.PersonalExamAlreadyStartedException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PersonalExamServiceTest extends AbstractSTPProducerApplicationTest {

    @MockBean
    private PersonalExamConnector personalExamConnector;

    @InjectMocks
    private PersonalExamService personalExamService;

    @Test
    public void shouldStartPersonalExam() {
        PersonalExam input = new PersonalExam();
        input.setId("5");
        input.setStatus(PersonalExamStatus.NOT_STARTED);
        Task task = new Task();
        task.setId(1L);
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.NOT_STARTED);
        answer.setTask(task);
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        input.setAnswers(answers);
        when(personalExamConnector.getById("5")).thenReturn(input);
        Task result = personalExamService.start("5");
        assertEquals(input.getAnswers().get(0).getTask().getId(), result.getId());
    }

    @Test
    public void shouldNotStartExamWhenItStarted() {
        PersonalExam input = new PersonalExam();
        input.setId("5");
        input.setStatus(PersonalExamStatus.IN_PROGRESS);
        when(personalExamConnector.getById("5")).thenReturn(input);
        assertThrows(PersonalExamAlreadyStartedException.class, () -> personalExamService.start("5"));
    }
}
