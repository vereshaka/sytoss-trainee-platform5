package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.lessons.dto.TaskConditionDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskConditionConvertorTest extends StpUnitTest {

    @Spy
    private TaskConditionConvertor taskConditionConvertor = new TaskConditionConvertor();

    @Test
    public void fromDTOTaskConditionConvertorTest() {
        TaskConditionDTO taskConditionDTO = new TaskConditionDTO();
        taskConditionDTO.setId(93L);
        taskConditionDTO.setValue("SQL");
        taskConditionDTO.setType(ConditionType.CONTAINS);
        TaskCondition taskCondition = new TaskCondition();
        taskConditionConvertor.fromDTO(taskConditionDTO, taskCondition);
        assertEquals(taskConditionDTO.getId(), taskCondition.getId());
        assertEquals(taskConditionDTO.getValue(), taskCondition.getValue());
        assertEquals(taskConditionDTO.getType(), taskCondition.getType());
    }

    @Test
    public void toDTODisciplineConvertorTest() {
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setId(93L);
        taskCondition.setValue("SQL");
        taskCondition.setType(ConditionType.CONTAINS);

        TaskConditionDTO taskConditionDTO = new TaskConditionDTO();
        taskConditionConvertor.toDTO(taskCondition, taskConditionDTO);
        assertEquals(taskConditionDTO.getId(), taskCondition.getId());
        assertEquals(taskConditionDTO.getValue(), taskCondition.getValue());
        assertEquals(taskConditionDTO.getType(), taskCondition.getType());
    }
}
