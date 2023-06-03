package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
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

/*    @Mock
    private TaskConvertor taskConvertor;*/

    @Test
    public void getTaskById() {
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

    @Test
    public void shouldCreateTask() {
        Mockito.doAnswer((Answer<TaskDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            TaskDTO result = (TaskDTO) args[0];
            result.setId(1L);
            return result;
        }).when(taskConnector).save(any(TaskDTO.class));
        Task input = new Task();
        input.setQuestion("question");
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(1L);
        input.setTaskDomain(taskDomain);
        Topic topic = new Topic();
        topic.setId(1L);
        input.setTopics(List.of(topic));
        Task result = taskService.create(input);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getQuestion(), result.getQuestion());
    }
}
