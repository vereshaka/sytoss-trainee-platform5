package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.TaskConditionNotFoundException;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.lessons.connectors.TaskConditionConnector;
import com.sytoss.lessons.convertors.TaskConditionConvertor;
import com.sytoss.lessons.dto.TaskConditionDTO;
import com.sytoss.stp.test.StpUnitTest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ConditionServiceTest extends StpUnitTest {

    @Mock
    private TaskConditionConnector taskConditionConnector;

    @Spy
    private TaskConditionConvertor taskConditionConvertor = new TaskConditionConvertor();

    @InjectMocks
    private TaskConditionService conditionService;

    @Test
    public void getTaskConditionById() {
        TaskConditionDTO input = new TaskConditionDTO();
        input.setId(1L);
        input.setValue("SQL");
        input.setType(ConditionType.CONTAINS);
        when(taskConditionConnector.getReferenceById(any())).thenReturn(input);
        TaskCondition result = conditionService.getById(1L);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getValue(), result.getValue());
        assertEquals(input.getType(), result.getType());
    }

    @Test
    public void shouldRaiseExceptionWhenTaskConditionNotExist() {
        when(taskConditionConnector.getReferenceById(1L)).thenThrow(new EntityNotFoundException());
        assertThrows(TaskConditionNotFoundException.class, () -> conditionService.getById(1L));
    }
}
