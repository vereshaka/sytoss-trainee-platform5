package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.EtalonIsNotValidException;
import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.exceptions.business.TaskDomainIsUsed;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bom.TaskDomainModel;
import com.sytoss.lessons.connectors.CheckTaskConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.TaskDomainConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.convertors.PumlConvertor;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.enums.ConvertToPumlParameters;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TaskDomainServiceTest extends StpUnitTest {

    @InjectMocks
    private TaskDomainService taskDomainService;

    @Mock
    private DisciplineService disciplineService;

    @Mock
    private TaskDomainConnector taskDomainConnector;

    @Mock
    private PersonalExamConnector personalExamConnector;

    @Mock
    private CheckTaskConnector checkTaskConnector;

    @Mock
    private TaskService taskService;

    @Spy
    private TaskDomainConvertor taskDomainConvertor = new TaskDomainConvertor(new DisciplineConvertor());

    @Spy
    private PumlConvertor pumlConvertor;

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
        taskDomain.setDescription("Anything");
        TaskDomain result = taskDomainService.create(1L, taskDomain);
        assertEquals(taskDomain.getName(), result.getName());
        assertEquals(taskDomain.getScript(), result.getScript());
        assertEquals(taskDomain.getDescription(), result.getDescription());
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
        taskDomainDTO.setDescription("Task Domain Description");
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        taskDomainDTO.setDiscipline(disciplineDTO);
        when(taskDomainConnector.getReferenceById(anyLong())).thenReturn(taskDomainDTO);
        TaskDomain result = taskDomainService.getById(1L);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("First Domain", result.getName());
        Assertions.assertEquals("Script Domain", result.getScript());
        Assertions.assertEquals("Task Domain Description", result.getDescription());
    }

    @Test
    public void shouldRaiseExceptionWhenTaskDomainNotExist() {
        when(taskDomainConnector.getReferenceById(1L)).thenThrow(new TaskDomainNotFoundException(1L));
        assertThrows(TaskDomainNotFoundException.class, () -> taskDomainService.getById(1L));
    }

    @Test
    public void shouldUpdate() {
        TaskDomainDTO taskDomain = new TaskDomainDTO();
        taskDomain.setId(1L);
        taskDomain.setName("First Domain");
        taskDomain.setScript("Script Domain");
        taskDomain.setDescription("Task Domain Description");
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        taskDomain.setDiscipline(disciplineDTO);
        when(personalExamConnector.taskDomainIsUsed(anyLong())).thenReturn(false);
        when(taskDomainConnector.getReferenceById(anyLong())).thenReturn(taskDomain);
        Mockito.doAnswer((Answer<TaskDomainDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            TaskDomainDTO result = (TaskDomainDTO) args[0];
            result.setName("new");
            result.setScript("new");
            result.setDescription("new");
            return result;
        }).when(taskDomainConnector).save(any(TaskDomainDTO.class));
        when(taskService.findByDomainId(anyLong())).thenReturn(new ArrayList<>());
        TaskDomain updateTaskDomain = new TaskDomain();
        taskDomain.setName("new");
        taskDomain.setScript("new");
        TaskDomain result = taskDomainService.update(taskDomain.getId(), updateTaskDomain);
        assertEquals("new", result.getName());
        assertEquals("new", result.getScript());
        assertEquals("new", result.getDescription());
    }

    @Test
    public void shouldNotUpdate() {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setName("First Domain");
        taskDomainDTO.setScript("Script Domain");
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName("new");
        taskDomainDTO.setDiscipline(disciplineDTO);
        List<PersonalExam> personalExams = new ArrayList<>();
        PersonalExam personalExam = new PersonalExam();
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(1L);
        taskDomain.setName("First Domain");
        taskDomain.setScript("Script Domain");
        Discipline discipline = new Discipline();
        discipline.setName("new");
        discipline.setTeacher(new Teacher());
        taskDomain.setDiscipline(discipline);
        com.sytoss.domain.bom.personalexam.Answer answer = new com.sytoss.domain.bom.personalexam.Answer();
        Task task = new Task();
        task.setTaskDomain(taskDomain);
        answer.setTask(task);
        answer.setStatus(AnswerStatus.NOT_STARTED);
        personalExam.getAnswers().add(answer);
        personalExam.setStatus(PersonalExamStatus.NOT_STARTED);
        personalExams.add(personalExam);
        when(personalExamConnector.taskDomainIsUsed(anyLong())).thenReturn(true);
        when(taskDomainConnector.getReferenceById(anyLong())).thenReturn(taskDomainDTO);
        TaskDomain updateTaskDomain = new TaskDomain();
        assertThrows(TaskDomainIsUsed.class, () -> taskDomainService.update(1L, updateTaskDomain));
    }

    @Test
    public void shouldNotUpdateWhenEtalonIsNotValid() {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(1L);
        taskDomain.setName("First Domain");
        taskDomain.setScript("Script Domain");
        Discipline discipline = new Discipline();
        discipline.setName("new");
        discipline.setTeacher(new Teacher());
        taskDomain.setDiscipline(discipline);
        Task task = new Task();
        task.setTaskDomain(taskDomain);
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setName("First Domain");
        taskDomainDTO.setScript("Script Domain");
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        taskDomainDTO.setDiscipline(disciplineDTO);
        when(taskDomainConnector.getReferenceById(anyLong())).thenReturn(taskDomainDTO);
        when(personalExamConnector.taskDomainIsUsed(anyLong())).thenReturn(false);
        when(taskService.findByDomainId(anyLong())).thenReturn(List.of(task));
        IsCheckEtalon isCheckEtalon = new IsCheckEtalon();
        isCheckEtalon.setException("error");
        when(checkTaskConnector.checkEtalon(any())).thenReturn(isCheckEtalon);
        TaskDomain updateTaskDomain = new TaskDomain();
        updateTaskDomain.setId(1L);
        assertThrows(EtalonIsNotValidException.class, () -> taskDomainService.update(1L, updateTaskDomain));
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

    @Test
    public void findTaskDomains() {
        TaskDomainDTO taskDomain = new TaskDomainDTO();
        taskDomain.setId(11L);
        taskDomain.setName("Test");
        DisciplineDTO discipline = new DisciplineDTO();
        discipline.setId(11L);
        discipline.setName("SQL");
        taskDomain.setDiscipline(discipline);
        List<TaskDomainDTO> input = new ArrayList<>();
        input.add(taskDomain);
        input.add(taskDomain);
        when(taskDomainConnector.findByDisciplineId(any())).thenReturn(input);
        List<TaskDomain> result = taskDomainService.findByDiscipline(11L);
        assertEquals(input.size(), result.size());
    }

    @Test
    void generatePngFromPuml() throws IOException {
        String pumlScript = readFromFile("puml/script.puml");
        assertNotNull(taskDomainService.generatePngFromPuml(pumlScript, ConvertToPumlParameters.DB));
        assertNotNull(taskDomainService.generatePngFromPuml(pumlScript, ConvertToPumlParameters.DATA));
        assertNotNull(taskDomainService.generatePngFromPuml(pumlScript, ConvertToPumlParameters.ALL));

    }

    @Test
    void getCountOfTasks() {
        Task task = new Task();
        task.setId(1L);
        when(taskService.findByDomainId((any()))).thenReturn(List.of(task));
        TaskDomainModel taskDomainModel = taskDomainService.getCountOfTasks(1L);
        assertEquals(1,taskDomainModel.getCountOfTasks());
    }

    private String readFromFile(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("script/" + path)).getFile());
        List<String> data = Files.readAllLines(Path.of(file.getPath()));
        return String.join("\n", data);
    }
}
