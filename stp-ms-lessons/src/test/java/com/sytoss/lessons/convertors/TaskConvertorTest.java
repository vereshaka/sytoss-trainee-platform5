package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.dto.*;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskConvertorTest extends AbstractJunitTest {

    @Spy
    private TaskConvertor taskConvertor = new TaskConvertor(new TaskDomainConvertor(new DisciplineConvertor()), new TopicConvertor(new DisciplineConvertor()), new TaskConditionConvertor());

    @Test
    public void fromDTOTaskConvertorTest() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(4L);
        taskDTO.setQuestion("What is SQL?");
        taskDTO.setEtalonAnswer("SQL is life");
        List<TopicDTO> topicDTOList = new ArrayList<>();
        TopicDTO topicDTO = new TopicDTO();
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(93L);
        disciplineDTO.setTeacherId(1L);
        topicDTO.setDiscipline(disciplineDTO);
        topicDTO.setId(9L);
        topicDTOList.add(topicDTO);
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(14L);
        taskDomainDTO.setDiscipline(disciplineDTO);
        taskDTO.setTaskDomain(taskDomainDTO);
        taskDTO.setTopics(topicDTOList);
        TaskConditionDTO taskConditionDTO = new TaskConditionDTO();
        taskConditionDTO.setId(1L);
        taskDTO.setConditions(List.of(taskConditionDTO));
        Task task = new Task();
        taskConvertor.fromDTO(taskDTO, task);

        assertEquals(taskDTO.getId(), task.getId());
        assertEquals(taskDTO.getQuestion(), task.getQuestion());
        assertEquals(taskDTO.getEtalonAnswer(), task.getEtalonAnswer());
    }

    @Test
    public void toDTOTaskConvertorTest() {
        Task task = new Task();
        task.setId(4L);
        task.setQuestion("What is SQL?");
        task.setEtalonAnswer("SQL is life");
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(14L);
        task.setTaskDomain(taskDomain);
        List<Topic> topics = new ArrayList<>();
        Topic topic = new Topic();
        Teacher teacher = new Teacher();
        teacher.setId(62L);
        Discipline discipline = new Discipline();
        discipline.setId(93L);
        discipline.setTeacher(teacher);
        topic.setDiscipline(discipline);
        topic.setId(9L);
        topics.add(topic);
        task.setTopics(topics);
        TaskCondition taskCondition = new TaskCondition();
        taskCondition. setId(1L);
        task.setTaskConditions(List.of(taskCondition));
        TaskDTO taskDTO = new TaskDTO();
        taskConvertor.toDTO(task, taskDTO);

        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getQuestion(), taskDTO.getQuestion());
        assertEquals(task.getEtalonAnswer(), taskDTO.getEtalonAnswer());
    }
}
