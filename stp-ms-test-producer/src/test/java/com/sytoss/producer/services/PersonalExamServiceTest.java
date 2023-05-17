package com.sytoss.producer.services;

import com.sytoss.producer.AbstractSTPProducerApplicationTest;
import com.sytoss.producer.bom.ExamConfiguration;
import com.sytoss.producer.bom.PersonalExam;
import com.sytoss.producer.connectors.MetadataConnectorImpl;
import com.sytoss.producer.connectors.PersonalExamConnector;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalExamServiceTest extends AbstractSTPProducerApplicationTest {

    @MockBean
    private MetadataConnectorImpl metadataConnector;

    @MockBean
    private PersonalExamConnector personalExamConnector;

    @InjectMocks
    private PersonalExamService personalExamService;

    @Test
    public void createExam(){
        ExamConfiguration examConfiguration = new ExamConfiguration();
        examConfiguration.setExamName("Exam First");
        examConfiguration.setDisciplineId(1L);
        List<Long> topics = new ArrayList<>();
        topics.add(1L);
        topics.add(2L);
        examConfiguration.setTopics(topics);
        examConfiguration.setQuantityOfTask(2);
        examConfiguration.setStudentId(1L);
        PersonalExam personalExam = personalExamService.create(examConfiguration);
        assertEquals(2, personalExam.getAnswers().size());
    }
}
