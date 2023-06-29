package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.stp.test.StpUnitTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskDomainConvertorTest extends StpUnitTest {

    private TaskDomainConvertor taskDomainConvertor = new TaskDomainConvertor(new DisciplineConvertor());

    @Test
    public void toDTOTaskDomainConvertorTest() {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(1L);
        taskDomain.setName("First Domain");
        taskDomain.setScript("Script Domain");
        Teacher teacher = new Teacher();
        teacher.setId(62L);
        Discipline discipline = new Discipline();
        discipline.setId(93L);
        discipline.setTeacher(teacher);
        taskDomain.setDiscipline(discipline);
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainConvertor.toDTO(taskDomain, taskDomainDTO);
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
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(93L);
        disciplineDTO.setTeacherId(1L);
        taskDomainDTO.setDiscipline(disciplineDTO);
        TaskDomain taskDomain = new TaskDomain();
        taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
        Assertions.assertEquals(taskDomainDTO.getId(), taskDomain.getId());
        Assertions.assertEquals(taskDomainDTO.getName(), taskDomain.getName());
        Assertions.assertEquals(taskDomainDTO.getScript(), taskDomain.getScript());
    }
}
