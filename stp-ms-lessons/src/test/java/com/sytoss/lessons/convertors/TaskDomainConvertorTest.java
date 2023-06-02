package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.dto.TaskDomainDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskDomainConvertorTest extends AbstractJunitTest {

    @Test
    public void toDTOTaskDomainConvertorTest() {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(1L);
        taskDomain.setName("First Domain");
        taskDomain.setScript("Script Domain");
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        new TaskDomainConvertor().toDTO(taskDomain, taskDomainDTO);
        Assertions.assertEquals(taskDomain.getId(), taskDomainDTO.getId());
        Assertions.assertEquals(taskDomain.getName(), taskDomainDTO.getName());
        Assertions.assertEquals(taskDomain.getScript(), taskDomainDTO.getScript());
    }

    @Test
    public void fromDTOTaskDomainConvertorTest() {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setName("First Domain");
        taskDomainDTO.setScript("Script Domain");
        TaskDomain taskDomain = new TaskDomain();
        new TaskDomainConvertor().fromDTO(taskDomainDTO, taskDomain);
        Assertions.assertEquals(taskDomainDTO.getId(), taskDomain.getId());
        Assertions.assertEquals(taskDomainDTO.getName(), taskDomain.getName());
        Assertions.assertEquals(taskDomainDTO.getScript(), taskDomain.getScript());
    }
}
