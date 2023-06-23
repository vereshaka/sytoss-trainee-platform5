package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.stp.test.StpUnitTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopicConvertorTest extends StpUnitTest {

    @Spy
    private TopicConvertor topicConvertor = new TopicConvertor(new DisciplineConvertor());

    @Test
    public void fromDTOTopicConvertorTest() {
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(93L);
        disciplineDTO.setTeacherId(1L);
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(1L);
        topicDTO.setName("Join");
        topicDTO.setDiscipline(disciplineDTO);
        Topic topic = new Topic();
        topicConvertor.fromDTO(topicDTO, topic);
        assertEquals(topicDTO.getId(), topic.getId());
        assertEquals(topicDTO.getName(), topic.getName());
        assertEquals(topicDTO.getDiscipline().getId(), topic.getDiscipline().getId());
    }

    @Test
    public void toDTOTopicConvertorTest() {
        Teacher teacher = new Teacher();
        teacher.setId(62L);
        Discipline discipline = new Discipline();
        discipline.setId(93L);
        discipline.setTeacher(teacher);
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Join");
        topic.setDiscipline(discipline);
        TopicDTO topicDTO = new TopicDTO();
        topicConvertor.toDTO(topic, topicDTO);
        assertEquals(topic.getId(), topicDTO.getId());
        assertEquals(topic.getName(), topicDTO.getName());
        assertEquals(topic.getDiscipline().getId(), topicDTO.getDiscipline().getId());
    }
}
