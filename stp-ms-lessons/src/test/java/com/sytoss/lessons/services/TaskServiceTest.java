package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.dto.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TaskServiceTest extends AbstractApplicationTest {

    @MockBean
    private TaskConnector taskConnector;

    @InjectMocks
    @Autowired
    private TaskService taskService;

    @Test
    public void getDisciplineById() {
        TaskDTO input = new TaskDTO();
        input.setId(1L);
        input.setQuestion("What is SQL?");
        input.setEtalonAnswer("SQL is life");
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        input.setTaskDomain(taskDomainDTO);
        List<TopicDTO> topicDTOList = new ArrayList<>();
        input.setTopics(topicDTOList);
        when(taskConnector.getReferenceById(any())).thenReturn(input);
        Task result = taskService.getById(1L);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getQuestion(), result.getQuestion());
    }
}
