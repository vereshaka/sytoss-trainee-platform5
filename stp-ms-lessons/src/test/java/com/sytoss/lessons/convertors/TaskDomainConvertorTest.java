package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskDomainConvertorTest extends StpUnitTest {

    private final TaskDomainConvertor taskDomainConvertor = new TaskDomainConvertor(new DisciplineConvertor());

    @Test
    public void toDTOTaskDomainConvertorTest() {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(1L);
        taskDomain.setName("First Domain");
        taskDomain.setDatabaseScript("Script Domain");
        taskDomain.setDataScript("Script Domain");
        taskDomain.setDescription("Task Domain Description");
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
        Assertions.assertEquals(taskDomain.getDatabaseScript(), taskDomainDTO.getDatabaseScript());
        Assertions.assertEquals(taskDomain.getDataScript(), taskDomainDTO.getDataScript());
        Assertions.assertEquals(taskDomain.getDescription(), taskDomainDTO.getDescription());
    }

    @Test
    public void fromDTOTaskDomainConvertorTest() {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setName("First Domain");
        taskDomainDTO.setDatabaseScript("Script Domain");
        taskDomainDTO.setDataScript("Script Domain");
        taskDomainDTO.setDescription("Task Domain Description");
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(93L);
        disciplineDTO.setTeacherId(1L);
        taskDomainDTO.setDiscipline(disciplineDTO);
        TaskDomain taskDomain = new TaskDomain();
        taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
        Assertions.assertEquals(taskDomainDTO.getId(), taskDomain.getId());
        Assertions.assertEquals(taskDomainDTO.getName(), taskDomain.getName());
        Assertions.assertEquals(taskDomainDTO.getDatabaseScript(), taskDomain.getDatabaseScript());
        Assertions.assertEquals(taskDomain.getDataScript(), taskDomainDTO.getDataScript());
        Assertions.assertEquals(taskDomainDTO.getDescription(), taskDomain.getDescription());
    }
}
