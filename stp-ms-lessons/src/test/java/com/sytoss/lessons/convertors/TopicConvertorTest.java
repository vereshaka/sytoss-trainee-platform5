package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import com.sytoss.lessons.dto.TopicDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopicConvertorTest extends AbstractJunitTest {

    @Spy
    private TopicConvertor topicConvertor = new TopicConvertor(new DisciplineConvertor(new TeacherConvertor()));

    @Test
    public void fromDTOTopicConvertorTest() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(62L);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(93L);
        disciplineDTO.setTeacher(teacherDTO);
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
