package com.sytoss.producer.services;

import com.sytoss.domain.bom.Answer;
import com.sytoss.domain.bom.AnswerStatus;
import com.sytoss.domain.bom.Grade;
import com.sytoss.domain.bom.PersonalExam;
import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersonalExamServiceTest extends AbstractSTPProducerApplicationTest {

    @MockBean
    private PersonalExamConnector personalExamConnector;

    @InjectMocks
    private PersonalExamService personalExamService;

    @Test
    public void shouldReturnSummery() {
        Grade firstGrade = new Grade();
        firstGrade.setId(1L);
        firstGrade.setValue(1f);
        firstGrade.setComment("answer correct");

        Grade secondGrade = new Grade();
        secondGrade.setId(2L);
        secondGrade.setValue(2f);
        secondGrade.setComment("answer correct");

        Answer firstAnswer = new Answer();
        firstAnswer.setId(1L);
        firstAnswer.setValue("SELECT * FROM Products");
        firstAnswer.setStatus(AnswerStatus.Answered);
        firstAnswer.setGrade(firstGrade);
        firstAnswer.setStatus(AnswerStatus.Graded);

        Answer secondAnswer = new Answer();
        secondAnswer.setId(2L);
        secondAnswer.setValue("SELECT * FROM Owners");
        secondAnswer.setStatus(AnswerStatus.Answered);
        secondAnswer.setGrade(secondGrade);
        secondAnswer.setStatus(AnswerStatus.Graded);

        PersonalExam personalExam = new PersonalExam();

        personalExam.setId("12345");
        personalExam.setName("DDL requests");
        personalExam.setAnswers(List.of(firstAnswer, secondAnswer));

        when(personalExamConnector.getById(personalExam.getId())).thenReturn(personalExam);

        PersonalExam returnPersonalExam = personalExamService.summary("12345");

        verify(personalExamConnector).getById("12345");
        Assertions.assertEquals("12345", returnPersonalExam.getId());
        Assertions.assertEquals(3f, returnPersonalExam.getSummaryGrade());
    }
}
