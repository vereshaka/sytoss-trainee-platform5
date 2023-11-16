package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.ExamNotFoundException;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.*;
import com.sytoss.lessons.dto.*;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExamServiceTest extends StpUnitTest {

    @InjectMocks
    private ExamService examService;

    @Mock
    private ExamConnector examConnector;

    @Mock
    private UserConnector userConnector;

    @Mock
    private ExamAssigneeService examAssigneeService;

    @Mock
    private PersonalExamConnector personalExamConnector;

    @Mock
    private ExamAssigneeConnector examAssigneeConnector;

    @Mock
    private ExamAssigneeToConnector examAssigneeToConnector;

    @Mock
    private TopicConnector topicConnector;

    @Mock
    private TaskConnector taskConnector;

    @Mock
    private DisciplineConnector disciplineConnector;

    @Mock
    private AnalyticsService analyticsService;

    @Mock
    private AnalyticsConnector analyticsConnector;

    @Spy
    private ExamConvertor examConvertor = new ExamConvertor(new TopicConvertor(new DisciplineConvertor()),
            new TaskConvertor(new TaskDomainConvertor(new DisciplineConvertor()), new TaskConditionConvertor(), new TopicConvertor(new DisciplineConvertor())), new ExamAssigneeConvertor());

    @Spy
    private ExamAssigneeConvertor examAssigneeConvertor = new ExamAssigneeConvertor();

    @Test
    public void shouldSaveExam() {
        Mockito.doAnswer((org.mockito.stubbing.Answer<ExamDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            ExamDTO result = (ExamDTO) args[0];
            result.setId(1L);
            return result;
        }).when(examConnector).save(any());

        Mockito.doAnswer((org.mockito.stubbing.Answer<TopicDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            Long id = (Long) args[0];
            TopicDTO result = new TopicDTO();
            result.setId(id);
            result.setDiscipline(new DisciplineDTO());
            return result;
        }).when(topicConnector).getReferenceById(anyLong());

        Mockito.doAnswer((org.mockito.stubbing.Answer<TaskDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            Long id = (Long) args[0];
            TaskDTO result = new TaskDTO();
            result.setId(id);
            result.setTaskDomain(new TaskDomainDTO());
            result.getTaskDomain().setDiscipline(new DisciplineDTO());
            return result;
        }).when(taskConnector).getReferenceById(anyLong());

        Teacher teacher = createTeacher("John", "Johns");

        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", teacher).build();
        Object credential = null;
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, credential);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Exam exam = new Exam();
        exam.setName("Exam");
        exam.setNumberOfTasks(10);
        Discipline discipline = new Discipline();
        discipline.setId(1L);
        Topic t1 = new Topic();
        t1.setId(1L);
        t1.setDiscipline(discipline);
        Topic t2 = new Topic();
        t2.setId(2L);
        t2.setDiscipline(discipline);
        exam.setTopics(List.of(t1, t2));
        Task task = new Task();
        task.setId(1L);
        task.setTaskDomain(new TaskDomain());
        exam.setTasks(List.of(task));

        Exam result = examService.save(exam);
        assertEquals(1L, result.getId());
    }

    @Test
    public void shouldReturnListOfExams() {
        Teacher user = new Teacher();
        user.setId(1L);
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", user).build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TopicDTO topicDTO = new TopicDTO();
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setGroupReferences(List.of(new GroupReferenceDTO()));
        topicDTO.setDiscipline(disciplineDTO);

        ExamDTO exam = new ExamDTO();
        exam.setName("Exam");

        exam.setNumberOfTasks(10);
        exam.setTopics(List.of(topicDTO));
        exam.setTeacherId(1L);

        when(examConnector.findByTeacherIdOrderByCreationDateDesc(1L)).thenReturn(List.of(exam));

        List<Exam> result = examService.findExams();
        assertEquals(1, result.size());
    }

    @Test
    public void shouldReturnExamsByTaskId() {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setDiscipline(new DisciplineDTO());
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setDiscipline(new DisciplineDTO());
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTopics(List.of(topicDTO));
        taskDTO.setTaskDomain(taskDomainDTO);
        ExamDTO examDTO = new ExamDTO();
        examDTO.setTopics(List.of(topicDTO));
        examDTO.setTasks(List.of(taskDTO));

        when(examConnector.findByTasks_Id(1L)).thenReturn(List.of(examDTO));
        List<Exam> examList = examService.getExamsByTaskId(1L);
        Assertions.assertNotEquals(0, examList.size());
    }

    @Test
    public void shouldDeleteExam() {
        doNothing().when(personalExamConnector).deletePersonalExamsByExamAssigneeId(any(List.class));
        doNothing().when(examConnector).deleteById(1L);
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setDiscipline(new DisciplineDTO());
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setDiscipline(new DisciplineDTO());
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTopics(List.of(topicDTO));
        taskDTO.setTaskDomain(taskDomainDTO);
        ExamDTO examDTO = new ExamDTO();
        examDTO.setId(1L);
        examDTO.setName("Exam 1");
        examDTO.setTopics(List.of(topicDTO));
        examDTO.setTasks(List.of(taskDTO));
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeDTO.setId(1L);
        ExamToStudentAssigneeDTO examToStudentAssigneeDTO = new ExamToStudentAssigneeDTO();
        examToStudentAssigneeDTO.setStudentId(1L);
        examAssigneeDTO.setExamAssigneeToDTOList(List.of(examToStudentAssigneeDTO));
        examDTO.setExamAssignees(List.of(examAssigneeDTO));
        when(examConnector.getReferenceById(1L)).thenReturn(examDTO);
        Exam result = examService.delete(1L);
        assertEquals(1L, result.getId());
        verify(personalExamConnector).deletePersonalExamsByExamAssigneeId(any(List.class));
        verify(examConnector).deleteById(1L);
    }

    @Test
    public void shouldReturnExamNotFoundWhenDelete() {
        when(examConnector.getReferenceById(1L)).thenThrow(new ExamNotFoundException(1L));
        assertThrows(ExamNotFoundException.class, () -> examService.delete(1L));
    }

    @Test
    public void shouldAssignExamToGroup() {
        Teacher teacher = createTeacher("Teacher", "1");
        Discipline discipline = createDiscipline("SQL", teacher);
        Group group = createGroup("AT-18", discipline);

        Exam exam = new Exam();
        exam.setId(1L);
        exam.setName("Exam");
        exam.setNumberOfTasks(10);
        exam.setTopics(List.of(new Topic(), new Topic()));
        exam.setTeacher(teacher);
        Task task = new Task();
        task.setTaskDomain(new TaskDomain());
        exam.setTasks(List.of(task));
        ExamDTO examDTO = new ExamDTO();
        examConvertor.toDTO(exam, examDTO);

        when(examConnector.getReferenceById(any())).thenReturn(examDTO);
        ExamAssignee examGroupAssignee = new ExamAssignee();
        examGroupAssignee.setRelevantFrom(new Date());
        examGroupAssignee.setRelevantTo(new Date());
        examGroupAssignee.getGroups().add(group);
        exam.getExamAssignees().add(examGroupAssignee);
        examConvertor.toDTO(exam, examDTO);
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeConvertor.toDTO(examGroupAssignee, examAssigneeDTO);
        when(examAssigneeConnector.save(any())).thenReturn(examAssigneeDTO);
        exam = examService.assign(examDTO.getId(), examGroupAssignee);
        assertEquals(examDTO.getId(), exam.getId());
        assertEquals(examDTO.getExamAssignees().size(), exam.getExamAssignees().size());
        assertEquals(examDTO.getExamAssignees().get(0).getId(), exam.getExamAssignees().get(0).getId());
    }

    @Test
    public void shouldAssignExamToStudents() {
        Teacher teacher = createTeacher("Teacher", "1");
        Student student = new Student();
        student.setId(1L);

        Exam exam = new Exam();
        exam.setId(1L);
        exam.setName("Exam");
        exam.setNumberOfTasks(10);
        exam.setTopics(List.of(new Topic(), new Topic()));
        exam.setTeacher(teacher);
        Task task = new Task();
        task.setTaskDomain(new TaskDomain());
        exam.setTasks(List.of(task));
        ExamDTO examDTO = new ExamDTO();
        examConvertor.toDTO(exam, examDTO);

        when(examConnector.getReferenceById(any())).thenReturn(examDTO);
        ExamAssignee examStudentAssignee = new ExamAssignee();
        examStudentAssignee.setRelevantFrom(new Date());
        examStudentAssignee.setRelevantTo(new Date());
        examStudentAssignee.getStudents().add(student);
        exam.getExamAssignees().add(examStudentAssignee);
        examConvertor.toDTO(exam, examDTO);
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeConvertor.toDTO(examStudentAssignee, examAssigneeDTO);
        when(examAssigneeConnector.save(any())).thenReturn(examAssigneeDTO);
        exam = examService.assign(examDTO.getId(), examStudentAssignee);
        assertEquals(examDTO.getId(), exam.getId());
        assertEquals(examDTO.getExamAssignees().size(), exam.getExamAssignees().size());
        assertEquals(examDTO.getExamAssignees().get(0).getId(), exam.getExamAssignees().get(0).getId());
    }

    @Test
    public void shouldReturnListOfExamAssigneesInOrder() {
        Teacher teacher = createTeacher("John", "Johns");
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", teacher).build();
        Object credential = null;
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, credential);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Exam exam = new Exam();
        exam.setId(1L);
        exam.setName("Exam");
        exam.setNumberOfTasks(10);
        exam.setTopics(List.of(new Topic(), new Topic()));
        exam.setTeacher(teacher);
        Task task = new Task();
        task.setTaskDomain(new TaskDomain());
        exam.setTasks(List.of(task));
        ExamDTO examDTO = new ExamDTO();
        examConvertor.toDTO(exam, examDTO);

        Date date = new Date(222222222);
        ExamAssignee examAssignee = new ExamAssignee();
        examAssignee.setExam(exam);
        examAssignee.setId(2L);
        examAssignee.setRelevantTo(date);
        examAssignee.setRelevantFrom(date);
        date = new Date(33333333);
        ExamAssignee examAssignee2 = new ExamAssignee();
        examAssignee2.setExam(exam);
        examAssignee2.setId(1L);
        examAssignee2.setRelevantTo(date);
        examAssignee2.setRelevantFrom(date);
        ExamAssignee examAssignee3 = new ExamAssignee();
        examAssignee3.setExam(exam);
        examAssignee3.setId(3L);
        examAssignee3.setRelevantTo(new Date());
        examAssignee3.setRelevantFrom(new Date());

        when(examConnector.findByTeacherIdOrderByCreationDateDesc(any())).thenReturn(List.of(examDTO));
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeConvertor.toDTO(examAssignee, examAssigneeDTO);
        ExamAssigneeDTO examAssigneeDTO2 = new ExamAssigneeDTO();
        examAssigneeConvertor.toDTO(examAssignee2, examAssigneeDTO2);
        ExamAssigneeDTO examAssigneeDTO3 = new ExamAssigneeDTO();
        examAssigneeConvertor.toDTO(examAssignee3, examAssigneeDTO3);
        when(examAssigneeConnector.findByExam_IdInOrderByRelevantFromDesc(any())).thenReturn(List.of(examAssigneeDTO3, examAssigneeDTO, examAssigneeDTO2));
        List<ExamAssignee> examAssignees = examService.findExamAssignees();
        assertEquals(3L, examAssignees.get(0).getId());
        assertEquals(2L, examAssignees.get(1).getId());
        assertEquals(1L, examAssignees.get(2).getId());
    }

    @Test
    public void shouldReturnExamReportModel() {
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeDTO.setId(1L);
        examAssigneeDTO.setExamAssigneeToDTOList(new ArrayList<>());
        examAssigneeDTO.setRelevantTo(new Date());
        examAssigneeDTO.setRelevantFrom(new Date());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setQuestion("Question");

        ExamDTO examDTO = new ExamDTO();
        examDTO.setName("Exam");
        examDTO.setMaxGrade(1);
        examDTO.setNumberOfTasks(1);

        when(examAssigneeConnector.getReferenceById(1L)).thenReturn(examAssigneeDTO);
        when(examConnector.findByExamAssignees_Id(1L)).thenReturn(examDTO);

        ExamReportModel result = examService.getReportInfo(1L);
        assertEquals("Exam", result.getExamName());
        assertEquals(1, result.getMaxGrade());
        assertEquals(1, result.getAmountOfTasks());
        assertEquals(examAssigneeDTO.getRelevantFrom(), result.getRelevantFrom());
        assertEquals(examAssigneeDTO.getRelevantTo(), result.getRelevantTo());
    }

    @Test
    public void shouldReturnListOfExamsByTopicId() {
        Teacher user = new Teacher();
        user.setId(1L);
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", user).build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(1L);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setGroupReferences(List.of(new GroupReferenceDTO()));
        topicDTO.setDiscipline(disciplineDTO);

        ExamDTO exam1 = createExam(1L, "Exam1", topicDTO);
        ExamDTO exam2 = createExam(2L, "Exam2", topicDTO);

        when(examConnector.getAllByTopicsContaining(any(TopicDTO.class))).thenReturn(List.of(exam1, exam2));

        List<Exam> result = examService.getExamsByTopic(topicDTO.getId());
        assertEquals(2, result.size());
    }

    private ExamDTO createExam(Long id, String name, TopicDTO topicDTO) {
        ExamDTO exam = new ExamDTO();
        exam.setId(id);
        exam.setName(name);
        exam.setTopics(List.of(topicDTO));
        return exam;
    }
}
