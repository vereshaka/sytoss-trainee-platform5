package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.convertors.TopicConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TopicServiceTest extends AbstractJunitTest {

    @Mock
    private TopicConnector topicConnector;


    private TopicConvertor topicConvertor;

    @Mock
    private DisciplineService disciplineService;

    @InjectMocks
    private TopicService topicService;

    @Test
    public void createExam() {
        List<TopicDTO> topics = new ArrayList<>();
        topics.add(createTopic("topic first"));
        topics.add(createTopic("topic second"));
        topics.add(createTopic("topic third"));
        when(topicConnector.findByDisciplineId(any())).thenReturn(topics);
        List<Topic> topicAnswer = topicService.findByDiscipline(1L);
        assertEquals(3, topicAnswer.size());
    }

    @Test
    public void shouldSaveTopic() {
        when(disciplineService.getById(1L)).thenReturn(createReference(1L));
        when(topicConnector.getByNameAndDisciplineId("CMS", 1L)).thenReturn(null);
        Mockito.doAnswer((Answer<TopicDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            TopicDTO result = (TopicDTO) args[0];
            result.setId(1L);
            return result;
        }).when(topicConnector).saveAndFlush(any(TopicDTO.class));
        Topic input = new Topic();
        input.setName("CMS");
        Topic result = topicService.create(1L, input);
        assertEquals(1L, result.getDiscipline().getId());
        assertEquals("CMS", result.getName());
    }

    @Test
    public void shouldReturnTopicById() {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(2L);
        topicDTO.setName("topic");
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        topicDTO.setDiscipline(disciplineDTO);
        when(topicConnector.getReferenceById(anyLong())).thenReturn(topicDTO);

        Topic result = topicService.getById(2L);
        assertEquals(2L, result.getId());
    }

    private TopicDTO createTopic(String name) {
        TopicDTO topic = new TopicDTO();
        topic.setName(name);
        return topic;
    }

    public Discipline createReference(Long id) {
        Discipline result = new Discipline();
        result.setId(id);
        result.setName("Test");
        return result;
    }
}
