package com.sytoss.producer.services;

import com.sytoss.domain.bom.exceptions.businessException.PersonalExamAlreadyStartedException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import com.sytoss.producer.connectors.MetadataConnectorImpl;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PersonalExamServiceTest extends AbstractSTPProducerApplicationTest {

    @MockBean
    private MetadataConnectorImpl metadataConnector;

    @MockBean
    private PersonalExamConnector personalExamConnector;

    @InjectMocks
    private PersonalExamService personalExamService;

    @Test
    public void createExam() {
        Mockito.doAnswer((org.mockito.stubbing.Answer<PersonalExam>) invocation -> {
            final Object[] args = invocation.getArguments();
            PersonalExam result = (PersonalExam) args[0];
            result.setId("1ada");
            return result;
        }).when(personalExamConnector).save(any());

        ExamConfiguration examConfiguration = new ExamConfiguration();
        examConfiguration.setExamName("Exam First");
        examConfiguration.setDisciplineId(1L);
        Discipline discipline = new Discipline();
        discipline.setId(1L);
        discipline.setName("SQL");
        when(metadataConnector.getDiscipline(anyLong())).thenReturn(discipline);
        when(metadataConnector.getTasksForTopic(1L)).thenReturn(List.of(createTask("Left Join?"), createTask("Is SQL cool?")));
        when(metadataConnector.getTasksForTopic(2L)).thenReturn(List.of(createTask("SELECT?"), createTask("SELECT?")));
        List<Long> topics = new ArrayList<>();
        topics.add(1L);
        topics.add(2L);
        examConfiguration.setTopics(topics);
        examConfiguration.setQuantityOfTask(2);
        examConfiguration.setStudentId(1L);
        PersonalExam personalExam = personalExamService.create(examConfiguration);
        assertEquals(2, personalExam.getAnswers().size());
        assertEquals("1ada", personalExam.getId());
    }

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
        input.setAnswers(Arrays.asList(answer));
        when(personalExamConnector.getById("5")).thenReturn(input);
        Task result = personalExamService.start("5");
        assertEquals(input.getAnswers().get(0).getTask().getId(), result.getId());
    }

    @Test
    public void shouldNotStartExamWhenItStarted() {
        PersonalExam input = new PersonalExam();
        input.setId("5");
        input.setStatus(PersonalExamStatus.IN_PROGRESS);
        Task task = new Task();
        task.setId(1L);
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.NOT_STARTED);
        answer.setTask(task);
        input.setAnswers(Arrays.asList(answer));
        when(personalExamConnector.getById("5")).thenReturn(input);
        assertThrows(PersonalExamAlreadyStartedException.class, () -> personalExamService.start("5"));
    }

    private Task createTask(String question) {
        Task task = new Task();
        task.setQuestion(question);
        return task;
    }
}
