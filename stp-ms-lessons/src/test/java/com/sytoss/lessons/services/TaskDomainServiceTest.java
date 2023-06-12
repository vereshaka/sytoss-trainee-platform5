package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.connectors.TaskDomainConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.convertors.TaskConvertor;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.convertors.TopicConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TaskDomainServiceTest extends AbstractJunitTest {

    @InjectMocks
    private TaskDomainService taskDomainService;

    @Mock
    private DisciplineService disciplineService;

    @Mock
    private TaskDomainConnector taskDomainConnector;

    @Spy
    private TaskDomainConvertor taskDomainConvertor = new TaskDomainConvertor(new DisciplineConvertor(), new TaskConvertor(new TopicConvertor(new DisciplineConvertor())));

    @Test
    public void shouldCreateTaskDomain() {
        when(disciplineService.getById(1L)).thenReturn(createReference(1L));
        when(taskDomainConnector.getByNameAndDisciplineId("TaskDomain first", 1L)).thenReturn(null);
        Mockito.doAnswer((Answer<TaskDomainDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            TaskDomainDTO result = (TaskDomainDTO) args[0];
            result.setId(1L);
            return result;
        }).when(taskDomainConnector).save(any(TaskDomainDTO.class));
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName("TaskDomain first");
        taskDomain.setScript("script");
        TaskDomain result = taskDomainService.create(1L, taskDomain);
        assertEquals(taskDomain.getName(), result.getName());
        assertEquals(taskDomain.getScript(), result.getScript());
    }

    @Test
    public void shouldNotCreateTaskDomainWhenItExist() {
        TaskDomain newTaskDomain = new TaskDomain();
        newTaskDomain.setName("TaskDomain first");
        newTaskDomain.setScript("script");
        when(disciplineService.getById(1L)).thenReturn(createReference(1L));
        when(taskDomainConnector.getByNameAndDisciplineId("TaskDomain first", 1L)).thenReturn(new TaskDomainDTO());
        assertThrows(TaskDomainAlreadyExist.class, () -> taskDomainService.create(1L, newTaskDomain));
    }

    @Test
    public void shouldReturnTaskDomainById() {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setName("First Domain");
        taskDomainDTO.setScript("Script Domain");
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        taskDomainDTO.setDiscipline(disciplineDTO);
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

    private Discipline createReference(Long id) {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Discipline discipline = new Discipline();
        discipline.setId(id);
        discipline.setTeacher(teacher);
        discipline.setName("first discipline");
        return discipline;
    }
}
