package com.sytoss.producer.services;

import com.sytoss.domain.bom.exceptions.business.PersonalExamAlreadyStartedException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.*;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.connectors.ImageConnector;
import com.sytoss.producer.connectors.MetadataConnector;
import com.sytoss.producer.connectors.PersonalExamConnector;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersonalExamServiceTest extends StpUnitTest {

    @Mock
    private MetadataConnector metadataConnector;

    @Mock
    private PersonalExamConnector personalExamConnector;

    @InjectMocks
    private PersonalExamService personalExamService;

    @Mock
    private ImageConnector imageConnector;

    @Test
    @Disabled
    public void createExam() {
        //TODO: STP-409: fix me
        Mockito.doAnswer((org.mockito.stubbing.Answer<PersonalExam>) invocation -> {
            final Object[] args = invocation.getArguments();
            PersonalExam result = (PersonalExam) args[0];
            result.setId("1ada");
            return result;
        }).when(personalExamConnector).save(any());

        ExamConfiguration examConfiguration = new ExamConfiguration();
      /*  examConfiguration.setExamName("Exam First");
        examConfiguration.setDisciplineId(1L);*/
        Discipline discipline = new Discipline();
        discipline.setId(1L);
        discipline.setName("SQL");
        when(metadataConnector.getTasksForTopic(1L)).thenReturn(List.of(createTask("Left Join?"), createTask("Is SQL cool?")));
        when(metadataConnector.getTasksForTopic(2L)).thenReturn(List.of(createTask("SELECT?"), createTask("SELECT?")));
        List<Long> topics = new ArrayList<>();
        topics.add(1L);
        topics.add(2L);
       /* examConfiguration.setTasks(topics);
        examConfiguration.setQuantityOfTask(2);*/
        Student student = new Student();
        student.setUid("notLongId");
        examConfiguration.setStudent(student);
        when(imageConnector.convertImage(anyString())).thenReturn(1L);
        PersonalExam personalExam = personalExamService.create(examConfiguration);
        assertEquals(2, personalExam.getAnswers().size());
        assertEquals("1ada", personalExam.getId());
    }

    @Test
    public void shouldReturnSummary() {
        PersonalExam personalExam = new PersonalExam();

        personalExam.setId("12345");
        personalExam.setName("DDL requests");
        personalExam.setAnswers(List.of(
                createAnswer("select * from products", 1, "answer correct", AnswerStatus.GRADED),
                createAnswer("select * from owners", 1, "answer correct", AnswerStatus.GRADED),
                createAnswer("select * from customers", 1, "answer correct", AnswerStatus.GRADED)
        ));

        when(personalExamConnector.getById(personalExam.getId())).thenReturn(personalExam);

        PersonalExam returnPersonalExam = personalExamService.summary("12345");

        verify(personalExamConnector).getById("12345");
        Assertions.assertEquals("12345", returnPersonalExam.getId());
        Assertions.assertEquals(3, returnPersonalExam.getSummaryGrade());
    }

    @Test
    public void shouldReturnSummaryIfAnswerNotGraded() {
        PersonalExam personalExam = new PersonalExam();

        personalExam.setId("12345");
        personalExam.setName("DDL requests");
        personalExam.setAnswers(List.of(
                createAnswer("select * from products", 1, "answer correct", AnswerStatus.GRADED),
                createAnswer("select * from owners", 0, "answer correct", AnswerStatus.ANSWERED),
                createAnswer("select * from customers", 1, "answer incorrect", AnswerStatus.GRADED)
        ));

        when(personalExamConnector.getById(personalExam.getId())).thenReturn(personalExam);

        PersonalExam returnPersonalExam = personalExamService.summary("12345");

        verify(personalExamConnector).getById("12345");
        Assertions.assertEquals("12345", returnPersonalExam.getId());
        Assertions.assertEquals(2, returnPersonalExam.getSummaryGrade());
    }

    @Test
    public void shouldStartPersonalExam() {
        PersonalExam input = new PersonalExam();
        input.setId("5");
        input.setStatus(PersonalExamStatus.NOT_STARTED);
        Task task = new Task();
        task.setId(1L);
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setDatabaseScript(".uml");
        task.setTaskDomain(taskDomain);
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.NOT_STARTED);
        answer.setTask(task);
        input.setAnswers(List.of(answer));
        input.setAmountOfTasks(1);
        input.setTime(10);
        Student student = new Student();
        student.setUid("1");
        input.setStudent(student);
        when(personalExamConnector.getById("5")).thenReturn(input);
        Mockito.doAnswer((org.mockito.stubbing.Answer<PersonalExam>) invocation -> {
            final Object[] args = invocation.getArguments();
            PersonalExam result = (PersonalExam) args[0];
            result.setId("1L");
            return result;
        }).when(personalExamConnector).save(any(PersonalExam.class));
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("id", "1").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Question result = personalExamService.start("5");
        assertEquals(input.getAnswers().get(0).getTask().getQuestion(), result.getTask().getQuestion());
    }

    @Test
    public void shouldNotStartExamWhenItStarted() {
        PersonalExam input = new PersonalExam();
        input.setId("5");
        input.setStatus(PersonalExamStatus.IN_PROGRESS);
        Task task = new Task();
        task.setId(1L);
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.NOT_STARTED);
        answer.setTask(task);
        Student student = new Student();
        student.setUid("1");
        input.setStudent(student);
        input.setAnswers(List.of(answer));
        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("id", "1").build();
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(personalExamConnector.getById("5")).thenReturn(input);
        assertThrows(PersonalExamAlreadyStartedException.class, () -> personalExamService.start("5"));
    }

    @Test
    public void shouldShouldReturnTrueWhenTaskDomainIsUsed() {
        when(personalExamConnector.countByAnswersTaskTaskDomainIdAndStatusNotLike(1L, PersonalExamStatus.FINISHED)).thenReturn(1);
        boolean isUsed = personalExamService.taskDomainIsUsed(1L);
        assertTrue(isUsed);
    }

    @Test
    public void shouldReturnPersonalExamsByUserId() throws ParseException {
        List<PersonalExam> exams = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        exams.add(createPersonalExam(1L, "Math", 5,
                new Date(format.parse("14.12.2018").getTime()),
                new Date(format.parse("14.12.2018").getTime())));
        exams.add(createPersonalExam(1L, "Math", 5,
                new Date(format.parse("14.12.2018").getTime()),
                new Date(format.parse("14.12.2018").getTime())));
        exams.add(createPersonalExam(1L, "Math", 5,
                new Date(format.parse("14.12.2018").getTime()),
                new Date(format.parse("14.12.2018").getTime())));
        exams.add(createPersonalExam(2L, "SQL", 10,
                new Date(format.parse("14.12.2018").getTime()),
                new Date(format.parse("14.12.2018").getTime())));
        when(personalExamConnector.getAllByStudent_Id(1L)).thenReturn(exams);

        List<PersonalExam> result = personalExamService.getByStudentId(1L);

        assertEquals(exams.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(exams.get(1).getExamId(), result.get(1).getExamId());
            assertEquals(exams.get(1).getAmountOfTasks(), result.get(1).getAmountOfTasks());
            assertEquals(exams.get(1).getStatus(), result.get(1).getStatus());
            assertEquals(exams.get(1).getAssignedDate(), result.get(1).getAssignedDate());
            assertEquals(exams.get(1).getStartedDate(), result.get(1).getStartedDate());
        }
    }

    private PersonalExam createPersonalExam(Long examId, String name, int amountOfTasks, Date assignedDate, Date startedDate) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setExamId(examId);
        personalExam.setName(name);
        personalExam.setStatus(PersonalExamStatus.FINISHED);
        personalExam.setAmountOfTasks(amountOfTasks);
        personalExam.setAssignedDate(assignedDate);
        personalExam.setStartedDate(startedDate);

        return personalExam;
    }

    private Task createTask(String question) {
        Task task = new Task();
        task.setQuestion(question);
        return task;
    }

    private Answer createAnswer(String value, float grade, String comment, AnswerStatus answerStatus) {
        Answer answer = new Answer();
        answer.setStatus(answerStatus);
        answer.setValue(value);
        answer.setGrade(createGrade(grade, comment));
        answer.setTeacherGrade(createGrade(grade, comment));
        return answer;
    }

    private Grade createGrade(float gradeValue, String comment) {
        Grade score = new Grade();
        score.setValue(gradeValue);
        score.setComment(comment);

        return score;
    }
}
