package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.convertors.*;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskServiceTest extends AbstractJunitTest {

    private final TaskConnector taskConnector = mock(TaskConnector.class);

    private final TaskService taskService = new TaskService(taskConnector,
            new TaskConvertor(new TaskDomainConvertor(), new TopicConvertor(new DisciplineConvertor(new TeacherConvertor()))));


    @Test
    public void shouldReturnTaskByTopicId() {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(1L);
        topicDTO.setName("Database");
        topicDTO.setDiscipline(new DisciplineDTO());

        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setName("Task Domain");

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setQuestion("Question");
        taskDTO.setEtalonAnswer("Answer");
        taskDTO.setTaskDomainDTO(taskDomainDTO);
        taskDTO.setTopics(List.of(topicDTO));

        when(taskConnector.findTasksByTopicsId(anyLong())).thenReturn(List.of(taskDTO));

        List<Task> result = taskService.findByTopicId(1L);
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals("Question", result.get(0).getQuestion());
        Assertions.assertEquals("Answer", result.get(0).getEtalonAnswer());
        Assertions.assertEquals(TaskDomain.class, result.get(0).getTaskDomain().getClass());
        Assertions.assertEquals(1, result.get(0).getTopics().size());
    }

    @Test
    public void shouldRaiseExceptionWhenTaskNotExist() {
        when(taskConnector.findTasksByTopicsId(2L)).thenThrow(new TaskNotFoundException(2L));
        assertThrows(TaskNotFoundException.class, () -> taskService.findByTopicId(2L));
    }
}
