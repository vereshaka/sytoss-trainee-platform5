package com.sytoss.producer.services;

import com.sytoss.checktaskshared.util.CheckTaskParameters;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.producer.AbstractJunitTest;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnswerServiceTest extends AbstractJunitTest {

    @Mock
    private PersonalExamConnector personalExamConnector;

    @Mock
    private CheckTaskConnector checkTaskConnector;

    @InjectMocks
    private AnswerService answerService;

    @Test
    public void testAnswer() {

        String taskAnswer = "taskAnswer";

        PersonalExam personalExam = new PersonalExam();
        personalExam.setId("examId");
        personalExam.setStatus(PersonalExamStatus.IN_PROGRESS);
        List<Answer> answers = new ArrayList<>();
        personalExam.setAnswers(answers);
        personalExam.setStudentId(77L);
        Answer currentAnswer = new Answer();
        Answer nextAnswer = new Answer();
        Task currentTask = new Task();
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(6L);
        currentTask.setId(5L);
        currentAnswer.setTask(currentTask);
        currentTask.setTaskDomain(taskDomain);
        when(personalExamConnector.getById(personalExam.getId())).thenReturn(personalExam);

        Mockito.doAnswer((org.mockito.stubbing.Answer<Answer>) invocation -> {
            final Object[] args = invocation.getArguments();
            Answer result = (Answer) args[0];
            return result;
        }).when(personalExamConnector).save(any(PersonalExam.class));

        when(personalExam.getCurrentAnswer()).thenReturn(currentAnswer);
        when(currentAnswer.getTask()).thenReturn(currentTask);
        when(currentTask.getTaskDomain()).thenReturn(taskDomain);
        when(personalExam.getNextAnswer()).thenReturn(nextAnswer);

        Answer result = answerService.answer(personalExam.getId(), personalExam.getStudentId(), taskAnswer);

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
