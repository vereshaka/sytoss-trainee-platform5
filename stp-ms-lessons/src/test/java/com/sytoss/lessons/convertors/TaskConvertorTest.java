package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.*;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskConvertorTest extends StpUnitTest {

    @Spy
    private TaskConvertor taskConvertor = new TaskConvertor(new TaskDomainConvertor(new DisciplineConvertor()), new TaskConditionConvertor(), new TopicConvertor(new DisciplineConvertor()));

    @Test
    public void fromDTOTaskConvertorTest() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(4L);
        taskDTO.setQuestion("What is SQL?");
        taskDTO.setEtalonAnswer("SQL is life");
        taskDTO.setCoef(2.0);
        taskDTO.setRequiredCommand("DROP");
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
        taskConditionDTO.setType(ConditionType.CONTAINS);
        taskConditionDTO.setValue("name");
        List<TaskConditionDTO> taskConditionDTOList = new ArrayList<>();
        taskConditionDTOList.add(taskConditionDTO);
        taskDTO.setConditions(taskConditionDTOList);
        Task task = new Task();
        taskConvertor.fromDTO(taskDTO, task);

        assertEquals(taskDTO.getId(), task.getId());
        assertEquals(taskDTO.getQuestion(), task.getQuestion());
        assertEquals(taskDTO.getEtalonAnswer(), task.getEtalonAnswer());
        assertEquals(taskDTO.getCoef(), task.getCoef());
        assertEquals(taskDTO.getRequiredCommand(), task.getRequiredCommand());
    }

    @Test
    public void toDTOTaskConvertorTest() {
        Task task = new Task();
        task.setId(4L);
        task.setQuestion("What is SQL?");
        task.setEtalonAnswer("SQL is life");
        task.setCoef(2.0);
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
        taskCondition.setId(1L);
        taskCondition.setType(ConditionType.CONTAINS);
        taskCondition.setValue("name");
        List<TaskCondition> taskConditionList = new ArrayList<>();
        taskConditionList.add(taskCondition);
        task.setTaskConditions(taskConditionList);
        TaskDTO taskDTO = new TaskDTO();
        taskConvertor.toDTO(task, taskDTO);

        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getQuestion(), taskDTO.getQuestion());
        assertEquals(task.getEtalonAnswer(), taskDTO.getEtalonAnswer());
        assertEquals(task.getCoef(), taskDTO.getCoef());
    }
}
