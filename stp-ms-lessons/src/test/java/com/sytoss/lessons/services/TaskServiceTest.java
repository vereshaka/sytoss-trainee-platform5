package com.sytoss.lessons.services;

import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bom.TaskDomainRequestParameters;
import com.sytoss.lessons.connectors.CheckTaskConnector;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.connectors.TaskConnector;
import com.sytoss.lessons.connectors.TaskDomainConnector;
import com.sytoss.lessons.convertors.*;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TaskServiceTest extends StpUnitTest {

    @Mock
    protected CheckTaskConnector checkTaskConnector;
    @Mock
    private TaskConnector taskConnector;
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TopicService topicService;
    @Mock
    private TaskDomainConnector taskDomainConnector;
    @Mock
    private DisciplineConnector disciplineConnector;
    @Spy
    private TaskConvertor taskConvertor = new TaskConvertor(new TaskDomainConvertor(new DisciplineConvertor()), new TopicConvertor(new DisciplineConvertor()), new TaskConditionConvertor());
    @Spy
    private PumlConvertor pumlConvertor;

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

        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setId(1L);

        Task input = new Task();
        input.setQuestion("question");
        input.setTaskDomain(taskDomain);
        input.setTopics(List.of(topic));
        input.setTaskConditions(List.of(taskCondition));

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
        Assertions.assertEquals(1, result.get(0).getTopics().size());
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
        Task result = taskService.assignTaskToTopic(1L, 1L);
        assertEquals(1, result.getTopics().size());
        assertEquals(topic.getName(), result.getTopics().get(0).getName());
    }

    @Test
    public void shouldReturnQueryResult() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("1", "1");
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setDataScript("Script");
        taskDomainDTO.setDatabaseScript("Script");
        QueryResult queryResult = new QueryResult(List.of(hashMap));
        when(checkTaskConnector.checkRequest(any())).thenReturn(queryResult);
        when(taskDomainConnector.getReferenceById(any())).thenReturn(taskDomainDTO);
        TaskDomainRequestParameters taskDomainRequestParameters = new TaskDomainRequestParameters();
        taskDomainRequestParameters.setRequest("");
        taskDomainRequestParameters.setTaskDomainId(1L);
        QueryResult result = taskService.getQueryResult(taskDomainRequestParameters);
        assertEquals("1", result.getResultMapList().get(0).get("1"));
    }
}
