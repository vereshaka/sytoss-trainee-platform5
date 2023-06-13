package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.TaskConditionNotFoundException;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.connectors.TaskConditionConnector;
import com.sytoss.lessons.dto.TaskConditionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ConditionServiceTest extends AbstractApplicationTest {

    @MockBean
    private TaskConditionConnector taskConditionConnector;

    @InjectMocks
    @Autowired
    private TaskConditionService conditionService;

    @Test
    public void getTaskConditionById() {
        TaskConditionDTO input = new TaskConditionDTO();
        input.setId(1L);
        input.setName("SQL");
        input.setType(ConditionType.CONTAINS);
        when(taskConditionConnector.getReferenceById(any())).thenReturn(input);
        TaskCondition result = conditionService.getById(1L);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getName(), result.getName());
        assertEquals(input.getType(), result.getType());
    }

    @Test
    public void shouldRaiseExceptionWhenTaskConditionNotExist() {
        when(taskConditionConnector.getReferenceById(1L)).thenThrow(new EntityNotFoundException());
        assertThrows(TaskConditionNotFoundException.class, () -> conditionService.getById(1L));
    }
}
