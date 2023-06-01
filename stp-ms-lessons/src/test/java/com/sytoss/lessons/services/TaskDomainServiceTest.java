package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.connectors.TaskDomainConnector;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.dto.TaskDomainDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TaskDomainServiceTest extends AbstractJunitTest {

    @InjectMocks
    private TaskDomainService taskDomainService;

    @Mock
    private TaskDomainConnector taskDomainConnector;

    @Spy
    private TaskDomainConvertor taskDomainConvertor;

    @Test
    public void shouldReturnTaskDomainById() {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setName("First Domain");
        taskDomainDTO.setScript("Script Domain");

        when(taskDomainConnector.getReferenceById(anyLong())).thenReturn(taskDomainDTO);

        TaskDomain result = taskDomainService.getById(1L);

        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("First Domain", result.getName());
        Assertions.assertEquals("Script Domain", result.getScript());
    }

    @Test
    public void shouldRaiseExceptionWhenTaskDomainNotExist() {
        when(taskDomainConnector.getReferenceById(1L)).thenThrow(new TaskDomainNotFoundException(1L));
        assertThrows(TaskDomainNotFoundException.class, () -> taskDomainService.getById(1L));
    }
}
