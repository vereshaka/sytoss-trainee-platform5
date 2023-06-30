package com.sytoss.producer.services;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AnswerServiceTest extends StpUnitTest {

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

        Long studentId = 1L;
        String examId = "4";
        String taskAnswer = "taskAnswer";

        PersonalExam input = new PersonalExam();
        input.setId(examId);
        input.setStatus(PersonalExamStatus.NOT_STARTED);
        Discipline discipline = new Discipline();
        discipline.setId(22L);
        input.setDiscipline(discipline);

        // first task and the answer that we will process and save
        Task task = new Task();
        task.setId(1L);
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(10L);
        taskDomain.setScript(".uml");
        task.setTaskDomain(taskDomain);
        Answer currentAnswer = new Answer();
        currentAnswer.setId(8L);
        currentAnswer.setStatus(AnswerStatus.NOT_STARTED);
        currentAnswer.setTask(task);

        // second task and the answer what we return at last
        Task nextTask = new Task();
        nextTask.setId(2L);
        TaskDomain nextTaskDomain = new TaskDomain();
        nextTaskDomain.setId(11L);
        nextTaskDomain.setScript(".uml");
        nextTask.setTaskDomain(nextTaskDomain);
        Answer nextAnswer = new Answer();
        nextAnswer.setId(9L);
        nextAnswer.setStatus(AnswerStatus.NOT_STARTED);
        nextAnswer.setTask(task);
        input.setAnswers(Arrays.asList(currentAnswer, nextAnswer));
        input.setAmountOfTasks(1);
        input.setTime(10);
        input.setStudentId(studentId);
        when(personalExamConnector.getById(examId)).thenReturn(input);
        Mockito.doAnswer((org.mockito.stubbing.Answer<PersonalExam>) invocation -> {
            final Object[] args = invocation.getArguments();
            PersonalExam result = (PersonalExam) args[0];
            result.setId(examId);
            return result;
        }).when(personalExamConnector).save(any(PersonalExam.class));

        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("id", "1").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        personalExamService.start("4");
        Answer result = answerService.answer(examId, taskAnswer);

        assertEquals(9, result.getId());
        assertNull(result.getValue());
        assertEquals("IN_PROGRESS", String.valueOf(result.getStatus()));
    }
}
