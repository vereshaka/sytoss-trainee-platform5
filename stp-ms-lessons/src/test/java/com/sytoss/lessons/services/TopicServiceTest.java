package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.convertors.TeacherConvertor;
import com.sytoss.lessons.convertors.TopicConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import com.sytoss.lessons.dto.TopicDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TopicServiceTest extends AbstractJunitTest {

    @Mock
    private DisciplineService disciplineService;

    @Mock
    private TopicConnector topicConnector;

    @Spy
    private TopicConvertor topicConvertor = new TopicConvertor(new DisciplineConvertor(new TeacherConvertor()));

    @InjectMocks
    private TopicService topicService;

    @Test
    public void createExam() {
        List<TopicDTO> topics = new ArrayList<>();
        topics.add(createTopicWithDiscipline("topic first"));
        topics.add(createTopicWithDiscipline("topic second"));
        topics.add(createTopicWithDiscipline("topic third"));
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
        disciplineDTO.setId(4L);
        disciplineDTO.setName("discipline");
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(88L);
        Topic topic = new Topic();
        disciplineDTO.setTeacher(teacherDTO);
        topicDTO.setDiscipline(disciplineDTO);
        when(topicConnector.getReferenceById(2L)).thenReturn(topicDTO);
        Topic result = topicService.getById(2L);
        assertEquals(2L, result.getId());
    }

    private TopicDTO createTopic(String name) {
        TopicDTO topic = new TopicDTO();
        topic.setName(name);
        return topic;
    }

    private TopicDTO createTopicWithDiscipline(String name) {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setName(name);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(4L);
        disciplineDTO.setName("first discipline");
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(88L);
        disciplineDTO.setTeacher(teacherDTO);
        topicDTO.setDiscipline(disciplineDTO);
        return topicDTO;
    }

    public Discipline createReference(Long id) {
        return Discipline.builder().id(id).name("first discipline").build();
    }
}
