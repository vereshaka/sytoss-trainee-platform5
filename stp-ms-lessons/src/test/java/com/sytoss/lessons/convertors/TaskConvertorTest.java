package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TaskConvertorTest extends AbstractJunitTest {

    private final TaskConvertor taskConvertor = new TaskConvertor(new TaskDomainConvertor(), new TopicConvertor(new DisciplineConvertor(new TeacherConvertor())));

    @Test
    public void toDTOTaskConvertorTest() {
        Task task = new Task();
        task.setId(1L);
        task.setQuestion("Question");
        task.setEtalonAnswer("Answer");

        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(1L);
        taskDomain.setName("TaskDomain");
        task.setTaskDomain(taskDomain);
        task.setTopics(List.of(new Topic()));
        TaskDTO taskDTO = new TaskDTO();
        taskConvertor.toDTO(task, taskDTO);

        Assertions.assertEquals(task.getId(), taskDTO.getId());
        Assertions.assertEquals(task.getQuestion(), taskDTO.getQuestion());
        Assertions.assertEquals(task.getEtalonAnswer(), taskDTO.getEtalonAnswer());
        Assertions.assertEquals(task.getTaskDomain().getId(), taskDTO.getTaskDomainDTO().getId());
        Assertions.assertEquals(task.getTaskDomain().getName(), taskDTO.getTaskDomainDTO().getName());
        Assertions.assertEquals(task.getTopics().size(), taskDTO.getTopics().size());
    }

    @Test
    public void fromDTOTaskConvertorTest() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setQuestion("Question");
        taskDTO.setEtalonAnswer("Answer");

        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setName("TaskDomain");

        taskDTO.setTaskDomainDTO(taskDomainDTO);

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setFirstName("Ivan");
        teacherDTO.setLastName("Ivanov");

        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(1L);
        disciplineDTO.setName("Database");
        disciplineDTO.setTeacher(teacherDTO);

        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(1L);
        topicDTO.setName("db");
        topicDTO.setDiscipline(disciplineDTO);

        taskDTO.setTopics(List.of(topicDTO));
        Task task = new Task();
        taskConvertor.fromDTO(taskDTO, task);

        Assertions.assertEquals(taskDTO.getId(), task.getId());
        Assertions.assertEquals(taskDTO.getQuestion(), task.getQuestion());
        Assertions.assertEquals(taskDTO.getEtalonAnswer(), task.getEtalonAnswer());
        Assertions.assertEquals(taskDTO.getTaskDomainDTO().getId(), task.getTaskDomain().getId());
        Assertions.assertEquals(taskDTO.getTaskDomainDTO().getName(), task.getTaskDomain().getName());
        Assertions.assertEquals(taskDTO.getTopics().size(), task.getTopics().size());
    }
}
