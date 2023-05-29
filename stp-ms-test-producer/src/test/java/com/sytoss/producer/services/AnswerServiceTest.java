package com.sytoss.producer.services;

import com.sytoss.checktaskshared.util.CheckTaskParameters;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.AbstractJunitTest;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
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

    @InjectMocks
    private PersonalExamService personalExamService;

    @Test
    public void testAnswer() {

        Long studentId = 77L;
        String examId = "4";
        String taskAnswer = "taskAnswer";

        PersonalExam input = new PersonalExam();
        input.setId(examId);
        input.setStatus(PersonalExamStatus.NOT_STARTED);
        Discipline discipline = new Discipline();
        discipline.setId(2L);
        input.setDiscipline(discipline);
        Task task = new Task();
        task.setId(1L);
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(9L);
        taskDomain.setScript(".uml");
        task.setTaskDomain(taskDomain);
        Answer currentAnswer = new Answer();
        currentAnswer.setId(8L);
        currentAnswer.setStatus(AnswerStatus.NOT_STARTED);
        currentAnswer.setTask(task);
        input.setAnswers(Arrays.asList(currentAnswer));
        input.setAmountOfTasks(1);
        input.setTime(10);
        input.setStudentId(studentId);
        when(personalExamConnector.getById(examId)).thenReturn(input);

        Mockito.doAnswer((org.mockito.stubbing.Answer<PersonalExam>) invocation -> {
            final Object[] args = invocation.getArguments();
            PersonalExam result = (PersonalExam) args[0];
            result.setId("1L");
            return result;
        }).when(personalExamConnector).save(any(PersonalExam.class));


        personalExamService.start("4", studentId);
        Answer result = answerService.answer(examId, studentId, taskAnswer);

        assertEquals("8", result.getId());
        assertEquals(taskAnswer, result.getValue());
        assertEquals("ANSWERED", result.getStatus());
    }
}
