package com.sytoss.producer.services;

import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import com.sytoss.producer.bom.*;
import com.sytoss.producer.connectors.MetadataConnectorImpl;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Mockito.doAnswer((Answer<PersonalExam>) invocation -> {
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
        List<Task> tasksOne = new ArrayList<>();
        List<Task> tasksTwo = new ArrayList<>();
        Task task = new Task();
        task.setQuestion("Left Join?");
        task.setEtalonAnswer("Yes");
        Task taskSecond = new Task();
        taskSecond.setQuestion("Is SQL cool?");
        List<Topic> topicsSecond = new ArrayList<>();
        taskSecond.setTopics(topicsSecond);
        taskSecond.setEtalonAnswer("Yes");
        Task taskThird = new Task();
        taskThird.setQuestion("SELECT?");
        List<Topic> topicsThird = new ArrayList<>();
        taskThird.setTopics(topicsThird);
        taskThird.setEtalonAnswer("Yes");
        Task taskFour = new Task();
        taskFour.setQuestion("SELECT?");
        taskFour.setEtalonAnswer("Yes");
        tasksOne.add(task);
        tasksOne.add(taskSecond);
        tasksTwo.add(taskThird);
        tasksTwo.add(taskFour);
        when(metadataConnector.getTasksForTopic(1L)).thenReturn(tasksOne);
        when(metadataConnector.getTasksForTopic(2L)).thenReturn(tasksTwo);
        List<Long> topics = new ArrayList<>();
        topics.add(1L);
        topics.add(2L);
        examConfiguration.setTopics(topics);
        examConfiguration.setQuantityOfTask(2);
        examConfiguration.setStudentId(1L);
        PersonalExam personalExam = personalExamService.create(examConfiguration);
        assertEquals(2, personalExam.getAnswers().size());
        assertEquals("1L", personalExam.getId());
    }
}
