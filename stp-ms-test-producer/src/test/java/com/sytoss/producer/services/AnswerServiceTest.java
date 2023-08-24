package com.sytoss.producer.services;

import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.MetadataConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class AnswerServiceTest extends StpUnitTest {

    @Mock
    private PersonalExamConnector personalExamConnector;

    @Mock
    private CheckTaskConnector checkTaskConnector;

    @Mock
    private MetadataConnector metadataConnector;

    @InjectMocks
    private AnswerService answerService;

    @InjectMocks
    private PersonalExamService personalExamService;

    @Mock
    private PumlConvertor pumlConvertor;

    @Test
    @Disabled
    public void testAnswer() {

        String studentId = "1L";
        String examId = "4";
        String taskAnswer = "taskAnswer";

        PersonalExam input = new PersonalExam();
        input.setId(examId);
        Discipline discipline = new Discipline();
        discipline.setId(22L);
        input.setDiscipline(discipline);
        // first task and the answer that we will process and save
        Task task = new Task();
        task.setId(1L);
        task.setCoef(2.0);
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(10L);
        taskDomain.setDatabaseScript(".uml");
        task.setTaskDomain(taskDomain);
        Answer currentAnswer = new Answer();
        currentAnswer.setId(8L);
        currentAnswer.setStatus(AnswerStatus.NOT_STARTED);
        currentAnswer.setTask(task);

        when(metadataConnector.getTaskById(anyLong())).thenReturn(task);
        when(metadataConnector.getTaskDomain(anyLong())).thenReturn(taskDomain);

        // second task and the answer what we return at last
        Task nextTask = new Task();
        nextTask.setId(2L);
        nextTask.setCoef(2.0);
        TaskDomain nextTaskDomain = new TaskDomain();
        nextTaskDomain.setId(11L);
        nextTaskDomain.setDatabaseScript(".uml");
        nextTask.setTaskDomain(nextTaskDomain);
        Answer nextAnswer = new Answer();
        nextAnswer.setId(9L);
        nextAnswer.setStatus(AnswerStatus.NOT_STARTED);
        nextAnswer.setTask(nextTask);
        input.setAnswers(Arrays.asList(currentAnswer, nextAnswer));
        input.setAmountOfTasks(1);
        input.setTime(10);
        Student student = new Student();
        student.setUid(studentId);
        input.setStudent(student);
        Score score = new Score();
        score.setValue(0);
        when(checkTaskConnector.checkAnswer(any())).thenReturn(score);
        when(personalExamConnector.getById(examId)).thenReturn(input);
        Mockito.doAnswer((org.mockito.stubbing.Answer<PersonalExam>) invocation -> {
            final Object[] args = invocation.getArguments();
            PersonalExam result = (PersonalExam) args[0];
            result.setId(examId);
            return result;
        }).when(personalExamConnector).save(any(PersonalExam.class));

        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("id", "1L").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        personalExamService.start("4");
        //TODO: igori: needs to be fixed
//        Answer result = answerService.answer(examId, taskAnswer);
//
//        assertEquals(9, result.getId());
//        assertNull(result.getValue());
//        assertEquals("IN_PROGRESS", String.valueOf(result.getStatus()));
    }
}
