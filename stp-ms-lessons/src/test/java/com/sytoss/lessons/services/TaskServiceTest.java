package com.sytoss.lessons.services;

import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bom.TaskDomainRequestParameters;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.*;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.stp.test.FileUtils;
import com.sytoss.stp.test.StpUnitTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TaskServiceTest extends StpUnitTest {

    private final ExamConnector examConnector = mock(ExamConnector.class);

    @Mock
    protected CheckTaskConnector checkTaskConnector;

    @Mock
    private TaskConnector taskConnector;

    @Mock
    private TopicService topicService;

    @Mock
    private TaskDomainConnector taskDomainConnector;

    @Mock
    private DisciplineConnector disciplineConnector;

    @Spy
    private TaskConvertor taskConvertor = new TaskConvertor(new TaskDomainConvertor(
            new DisciplineConvertor()), new TaskConditionConvertor(), new TopicConvertor(new DisciplineConvertor()));

    @Spy
    private PumlConvertor pumlConvertor;

    @Mock
    private ExamConvertor examConvertor;

    @Mock
    private PersonalExamConnector personalExamConnector;

    @Mock
    private ExamAssigneeConvertor examAssigneeConvertor;

    @Mock
    private ExamAssigneeConnector examAssigneeConnector;

    @Mock
    private ExamAssigneeToConnector examAssigneeConnectorTo;

    @Mock
    private UserConnector userConnector;

    @Mock
    private ExamAssigneeService examAssigneeService;

    @Mock
    private TopicConnector topicConnector;


    @Spy
    private ExamService examService = new ExamService(
            examConnector, examConvertor, userConnector,
            personalExamConnector, examAssigneeConvertor,
            examAssigneeConnector, disciplineConnector,
            examAssigneeConnectorTo, examAssigneeService,
            topicConnector, taskConnector
    );

    @InjectMocks
    private TaskService taskService;

    @Test
    public void getTaskById() {
        TaskDTO input = new TaskDTO();
        input.setId(1L);
        input.setQuestion("What is SQL?");
        input.setEtalonAnswer("SQL is life");
        input.setCoef(2.0);
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        taskDomainDTO.setDiscipline(disciplineDTO);
        input.setTaskDomain(taskDomainDTO);
        List<TopicDTO> topicDTOList = new ArrayList<>();
        input.setTopics(topicDTOList);
        when(taskConnector.getReferenceById(any())).thenReturn(input);
        Task result = taskService.getById(1L);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getQuestion(), result.getQuestion());
    }

    @Test
    public void shouldCreateTask() {
        Mockito.doAnswer((Answer<TaskDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            TaskDTO result = (TaskDTO) args[0];
            result.setId(1L);
            return result;
        }).when(taskConnector).save(any(TaskDTO.class));

        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(1L);
        taskDomainDTO.setDiscipline(disciplineDTO);
        when(taskDomainConnector.getReferenceById(anyLong())).thenReturn(taskDomainDTO);

        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setId(1L);

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Discipline discipline = new Discipline();
        discipline.setId(1L);
        discipline.setTeacher(teacher);
        taskDomain.setDiscipline(discipline);

        Topic topic = new Topic();
        topic.setId(1L);
        topic.setDiscipline(discipline);

        Task input = new Task();
        input.setQuestion("question");
        input.setTaskDomain(taskDomain);
        input.setTopics(List.of(topic));
        input.setRequiredCommand("");

        Task result = taskService.create(input);

        assertEquals(input.getId(), result.getId());
        assertEquals(input.getQuestion(), result.getQuestion());
    }

    @Test
    public void shouldReturnTasksByTopicId() {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(1L);
        topicDTO.setName("Database");
        topicDTO.setDiscipline(new DisciplineDTO());

        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setName("Task Domain");
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        taskDomainDTO.setDiscipline(disciplineDTO);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setQuestion("Question");
        taskDTO.setEtalonAnswer("Answer");
        taskDTO.setCoef(2.0);
        taskDTO.setTaskDomain(taskDomainDTO);
        taskDTO.setTopics(List.of(topicDTO));

        when(taskConnector.findByTopicsId(anyLong())).thenReturn(List.of(taskDTO));

        List<Task> result = taskService.findByTopicId(1L);
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals("Question", result.get(0).getQuestion());
        Assertions.assertEquals("Answer", result.get(0).getEtalonAnswer());
        Assertions.assertEquals(TaskDomain.class, result.get(0).getTaskDomain().getClass());
    }

    @Test
    public void shouldAssignTaskToTopic() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        Mockito.doAnswer((Answer<TaskDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            TaskDTO result = (TaskDTO) args[0];
            return result;
        }).when(taskConnector).save(any(TaskDTO.class));
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(1L);
        disciplineDTO.setTeacherId(1L);
        taskDomainDTO.setDiscipline(disciplineDTO);
        taskDTO.setTaskDomain(new TaskDomainDTO());
        taskDTO.setQuestion("new");
        taskDTO.setEtalonAnswer("one");
        taskDTO.setCoef(2.0);
        taskDTO.setTaskDomain(taskDomainDTO);
        when(taskConnector.getReferenceById(anyLong())).thenReturn(taskDTO);
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Database");
        Discipline discipline = new Discipline();
        discipline.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        discipline.setTeacher(teacher);
        topic.setDiscipline(discipline);
        when(topicService.getById(anyLong())).thenReturn(topic);
        List<Task> result = taskService.assignTasksToTopic(1L, List.of(1L));
        for (Task task : result) {
            assertEquals(1, task.getTopics().size());
            assertEquals(topic.getName(), task.getTopics().get(0).getName());
        }

    }

    @Test
    public void shouldReturnQueryResult() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("1", "1");
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setDatabaseScript(FileUtils.readFromFile("puml/database.puml"));
        taskDomainDTO.setDataScript(FileUtils.readFromFile("puml/data.puml"));
        QueryResult queryResult = new QueryResult();
        queryResult.setHeader(List.of("1"));
        queryResult.addValues(hashMap);
        when(checkTaskConnector.checkRequest(any())).thenReturn(queryResult);
        when(taskDomainConnector.getReferenceById(any())).thenReturn(taskDomainDTO);
        TaskDomainRequestParameters taskDomainRequestParameters = new TaskDomainRequestParameters();
        taskDomainRequestParameters.setRequest("");
        taskDomainRequestParameters.setTaskDomainId(1L);
        QueryResult result = taskService.getQueryResult(taskDomainRequestParameters);
        assertEquals("1", result.getValue(0, "1"));
    }

    @Test
    public void shouldDeleteTask() {
        ExamDTO examDTO = mock(ExamDTO.class);
        List<TaskDTO> taskDTOList = mock(List.class);
        TaskDTO taskDTO = new TaskDTO();
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(1L);
        disciplineDTO.setTeacherId(1L);
        taskDomainDTO.setDiscipline(disciplineDTO);
        taskDTO.setId(1L);
        taskDTO.setTaskDomain(new TaskDomainDTO());
        taskDTO.setQuestion("new");
        taskDTO.setEtalonAnswer("one");
        taskDTO.setCoef(2.0);
        taskDTO.setTaskDomain(taskDomainDTO);
        examDTO.setTasks(List.of(taskDTO));

        when(taskConnector.getReferenceById(1L)).thenReturn(taskDTO);
        when(examConnector.findByTasks_Id(1L)).thenReturn(List.of(examDTO));
        when(examDTO.getTasks()).thenReturn(taskDTOList);
        when(taskDTOList.remove(any())).thenReturn(true);
        doNothing().when(taskConnector).deleteById(1L);

        Task task = taskService.deleteTask(1L);

        assertEquals(1L, task.getId());
        verify(examConnector).findByTasks_Id(1L);
        verify(examConnector).save(examDTO);
    }

    @Test
    @Disabled
    public void shouldReturnTopics() {
        when(taskConnector.getReferenceById(1L)).thenReturn(createTaskDTO());
        List<Topic> topics = taskService.getTopics(1L);
        assertNotEquals(0, topics.size());
    }

    @Test
    public void shouldThrowTaskNotFoundWhenGetTopics() {
        when(taskConnector.getReferenceById(1L)).thenThrow(new TaskNotFoundException(1L));
        assertThrows(TaskNotFoundException.class, () -> taskService.getTopics(1L));
    }

    private TaskDTO createTaskDTO() {
        TopicDTO topic = createTopicDTO();

        TaskDTO task = new TaskDTO();
        task.setId(1L);
        task.setCoef(1.0);
        task.setQuestion("Question 1");
        task.setTopics(List.of(topic));
        task.setTaskDomain(createTaskDomainDTO());

        return task;
    }

    private TopicDTO createTopicDTO() {
        TopicDTO topic = new TopicDTO();
        topic.setId(1L);
        topic.setDiscipline(new DisciplineDTO());
        topic.setName("Name");
        return topic;
    }

    private TaskDomainDTO createTaskDomainDTO() {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setDiscipline(new DisciplineDTO());
        return taskDomainDTO;
    }
}
