package com.sytoss.producer.services;

import com.sytoss.checktaskshared.util.CheckTaskParameters;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnswerServiceTest extends AbstractSTPProducerApplicationTest {

    @MockBean
    private PersonalExamConnector personalExamConnector;

    @MockBean
    private CheckTaskConnector checkTaskConnector;

    @InjectMocks
    private AnswerService answerService;

    @Test
    public void testAnswer() {

        String examId = "examId";
        String taskAnswer = "taskAnswer";

        PersonalExam personalExamMock = Mockito.mock(PersonalExam.class);
        List<Answer> answers = new ArrayList<>();
        personalExamMock.setAnswers(answers);
        Answer currentAnswer = Mockito.mock(Answer.class);
        Answer nextAnswer = Mockito.mock(Answer.class);
        Task currentTask = Mockito.mock(Task.class);
        TaskDomain taskDomain = Mockito.mock(TaskDomain.class);
        taskDomain.setId(6L);
        currentTask.setId(5L);
        currentAnswer.setTask(currentTask);
        currentTask.setTaskDomain(taskDomain);

        when(personalExamConnector.getById(examId)).thenReturn(personalExamMock);
        when(personalExamMock.getCurrentAnswer()).thenReturn(currentAnswer);
        when(currentAnswer.getTask()).thenReturn(currentTask);
        when(currentTask.getTaskDomain()).thenReturn(taskDomain);
        when(personalExamMock.getNextAnswer()).thenReturn(nextAnswer);

        Answer result = answerService.answer(examId, taskAnswer);

        verify(checkTaskConnector).checkAnswer(any(CheckTaskParameters.class));
        verify(personalExamConnector, times(2)).save(any(PersonalExam.class));

        assertEquals(nextAnswer, result);
        assertEquals(nextAnswer.getId(), result.getId());
        assertEquals(nextAnswer.getValue(), result.getValue());
        assertEquals(nextAnswer.getTask(), result.getTask());
        assertEquals(nextAnswer.getStatus(), result.getStatus());
        assertEquals(nextAnswer.getGrade(), result.getGrade());
    }
}
