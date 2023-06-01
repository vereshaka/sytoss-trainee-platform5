package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.connectors.TaskDomainConnector;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.dto.TaskDomainDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TaskDomainServiceTest extends AbstractJunitTest {

    @InjectMocks
    private TaskDomainService taskDomainService;

    @Mock
    private TaskDomainConnector taskDomainConnector;

    @Mock
    private TaskDomainConvertor taskDomainConvertor;

    @Test
    public void shouldCreateTaskDomain() {
        Mockito.doAnswer((org.mockito.stubbing.Answer<TaskDomainDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            TaskDomainDTO result = (TaskDomainDTO) args[0];
            result.setId(1L);
            return result;
        }).when(taskDomainConnector).save(any());
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName("new");
        taskDomain.setScript("script");
        when(taskDomainConnector.getByName(anyString())).thenReturn(null);
        TaskDomain result = taskDomainService.create(taskDomain);
        assertEquals(taskDomain.getName(), result.getName());
        assertEquals(taskDomain.getScript(), result.getScript());
    }

    @Test
    public void shouldNotCreateTaskDomainWhenItExist() {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName("new");
        taskDomain.setScript("script");
        when(taskDomainConnector.getByName(anyString())).thenReturn(new TaskDomainDTO());
        assertThrows(TaskDomainAlreadyExist.class, () -> taskDomainService.create(taskDomain));
    }
}
