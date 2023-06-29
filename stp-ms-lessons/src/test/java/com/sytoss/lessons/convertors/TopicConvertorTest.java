package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.stp.test.StpUnitTest;
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
        topicDTO.setShortDescription("test short description");
        topicDTO.setFullDescription("test full description");
        topicDTO.setDuration(1.5);
        byte[] iconBytes = {0x01, 0x02, 0x03};
        topicDTO.setIcon(iconBytes);
        topicDTO.setDiscipline(disciplineDTO);
        Topic topic = new Topic();
        topicConvertor.fromDTO(topicDTO, topic);
        assertEquals(topicDTO.getId(), topic.getId());
        assertEquals(topicDTO.getName(), topic.getName());
        assertEquals(topicDTO.getShortDescription(), topic.getShortDescription());
        assertEquals(topicDTO.getFullDescription(), topic.getFullDescription());
        assertEquals(topicDTO.getDuration(), topic.getDuration());
        assertEquals(topicDTO.getIcon(), topic.getIcon());
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
        topic.setShortDescription("test short description");
        topic.setFullDescription("test full description");
        topic.setDuration(1.5);
        byte[] iconBytes = {0x01, 0x02, 0x03};
        topic.setIcon(iconBytes);
        topic.setDiscipline(discipline);
        TopicDTO topicDTO = new TopicDTO();
        topicConvertor.toDTO(topic, topicDTO);
        assertEquals(topic.getId(), topicDTO.getId());
        assertEquals(topic.getName(), topicDTO.getName());
        assertEquals(topic.getShortDescription(), topicDTO.getShortDescription());
        assertEquals(topic.getFullDescription(), topicDTO.getFullDescription());
        assertEquals(topic.getDuration(), topicDTO.getDuration());
        assertEquals(topic.getIcon(), topicDTO.getIcon());
        assertEquals(topic.getDiscipline().getId(), topicDTO.getDiscipline().getId());
    }
}
