package com.sytoss.producer.services;

import com.sytoss.domain.bom.exceptions.business.PersonalExamAlreadyStartedException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Disabled
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
        Assertions.assertEquals(3, returnPersonalExam.getTeacherGrade());
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
        Assertions.assertEquals(2, returnPersonalExam.getTeacherGrade());
    }

    @Test
    @Disabled
    public void shouldStartPersonalExam() {
        PersonalExam input = new PersonalExam();
        input.setId("5");
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
    }

    @Test
    public void shouldNotStartExamWhenItStarted() {
        PersonalExam input = new PersonalExam();
        input.setId("5");
        input.start();
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
    @Disabled
    public void shouldShouldReturnTrueWhenTaskDomainIsUsed() {
        when(personalExamConnector.countByAnswersTaskTaskDomainIdAndStatusNotLike(1L, PersonalExamStatus.FINISHED)).thenReturn(1);
        boolean isUsed = personalExamService.taskDomainIsUsed(1L);
        assertTrue(isUsed);
    }

    @Test
    public void shouldReturnPersonalExamsByUserId() throws ParseException {
        List<PersonalExam> exams = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        exams.add(createPersonalExam(1L, "Math", 5, format.parse("14.12.2018"), format.parse("14.12.2018")));
        exams.add(createPersonalExam(1L, "Math", 5, format.parse("14.12.2018"), format.parse("14.12.2018")));
        exams.add(createPersonalExam(1L, "Math", 5, format.parse("14.12.2018"), format.parse("14.12.2018")));
        exams.add(createPersonalExam(2L, "SQL", 10, format.parse("14.12.2018"), format.parse("14.12.2018")));
        when(personalExamConnector.getAllByStudent_IdOrderByAssignedDateDesc(1L)).thenReturn(exams);

        List<PersonalExam> result = personalExamService.getByStudentId(1L);

        assertEquals(exams.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(exams.get(1).getExamAssigneeId(), result.get(1).getExamAssigneeId());
            assertEquals(exams.get(1).getAmountOfTasks(), result.get(1).getAmountOfTasks());
            assertEquals(exams.get(1).getStatus(), result.get(1).getStatus());
            assertEquals(exams.get(1).getAssignedDate(), result.get(1).getAssignedDate());
            assertEquals(exams.get(1).getStartedDate(), result.get(1).getStartedDate());
        }
    }

    @Test
    public void shouldReturnQuestionImage() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId("123-abc-def");
        Task task = createTask("question");
        Answer answer = createAnswer("answer", 10f, "True", AnswerStatus.IN_PROGRESS);
        answer.setTask(task);
        personalExam.setAnswers(List.of(answer));

        when(personalExamConnector.getById("123-abc-def")).thenReturn(personalExam);

        byte[] result = personalExamService.getQuestionImage("123-abc-def");

        assertNotNull(result);
    }

    @Test
    public void shouldDeletePersonalExamsByExamAssigneeId() {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId("123-abc-def");
        Task task = createTask("question");
        Answer answer = createAnswer("answer", 10f, "True", AnswerStatus.IN_PROGRESS);
        answer.setTask(task);
        personalExam.setAnswers(List.of(answer));

        when(personalExamConnector.getAllByExamAssigneeId(1L)).thenReturn(List.of(personalExam));
        doNothing().when(personalExamConnector).deleteAll(any());

        List<PersonalExam> personalExams = personalExamService.deleteByExamAssigneeId(1L);
        assertEquals(1, personalExams.size());
        verify(personalExamConnector).deleteAll(any());
    }

    private PersonalExam createPersonalExam(Long examAssigneeId, String name, int amountOfTasks, Date assignedDate, Date startedDate) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setExamAssigneeId(examAssigneeId);
        personalExam.setName(name);
        personalExam.finish();
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

    @Test
    public void shouldReviewTestByAnswers() {
        Answer answer1 = createAnswer("select * from products", 1, "answer correct", AnswerStatus.GRADED);
        Answer answer2 = createAnswer("select * from products", 1, "answer correct", AnswerStatus.GRADED);

        answer1.setId(1L);
        answer2.setId(2L);

        PersonalExam personalExam = new PersonalExam();
        personalExam.setId("1");
        personalExam.setName("DDL requests");
        personalExam.setAnswers(List.of(answer1));

        PersonalExam personalExam2 = new PersonalExam();
        personalExam2.setId("2");
        personalExam2.setName("DDL requests");
        personalExam2.setAnswers(List.of(answer2));

        when(personalExamConnector.getById("1")).thenReturn(personalExam);
        when(personalExamConnector.getById("2")).thenReturn(personalExam2);

        answer1.setTeacherGrade(new Grade(20, "ok"));
        answer2.setTeacherGrade(new Grade(10, "ok"));

        ExamAssigneeAnswersModel examAssigneeAnswersModel = new ExamAssigneeAnswersModel();
        ReviewGradeModel gradeModel1 = new ReviewGradeModel();
        gradeModel1.setPersonalExamId(personalExam.getId());
        gradeModel1.setAnswer(answer1);
        ReviewGradeModel gradeModel2 = new ReviewGradeModel();
        gradeModel2.setPersonalExamId(personalExam2.getId());
        gradeModel2.setAnswer(answer2);
        examAssigneeAnswersModel.setGrades(List.of(gradeModel1, gradeModel2));

        ExamAssigneeAnswersModel answers = personalExamService.reviewByAnswers(examAssigneeAnswersModel);

        Assertions.assertEquals(answers.getGrades().size(), 2);
        Assertions.assertEquals("1", answers.getGrades().get(0).getPersonalExamId());
        Assertions.assertEquals(20, answers.getGrades().get(0).getAnswer().getTeacherGrade().getValue());
        Assertions.assertEquals("2", answers.getGrades().get(1).getPersonalExamId());
        Assertions.assertEquals(10, answers.getGrades().get(1).getAnswer().getTeacherGrade().getValue());
    }

    @Test
    public void shouldReturnPersonalExam() {
        PersonalExam personalExam = new PersonalExam();

        personalExam.setId("12345");
        personalExam.setName("DDL requests");
        personalExam.setAnswers(List.of(
                createAnswer("select * from products", 1, "answer correct", AnswerStatus.GRADED),
                createAnswer("select * from owners", 1, "answer correct", AnswerStatus.GRADED),
                createAnswer("select * from customers", 1, "answer correct", AnswerStatus.GRADED)
        ));

        when(personalExamConnector.getById(personalExam.getId())).thenReturn(personalExam);

        PersonalExam returnPersonalExam = personalExamService.getById(personalExam.getId());
        Assertions.assertEquals("12345", returnPersonalExam.getId());
    }


    @Test
    void updateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setQuestion("select all from products");

        Answer answer1 = new Answer();
        answer1.setValue("select * from products");
        answer1.setGrade(new Grade(1, "answer correct"));
        answer1.setStatus(AnswerStatus.GRADED);
        answer1.setTask(task);
        Answer answer2 = new Answer();
        answer2.setValue("select * from products");
        answer2.setGrade(new Grade(1, "answer correct"));
        answer2.setStatus(AnswerStatus.GRADED);
        answer2.setTask(task);
        answer1.setId(1L);
        answer2.setId(2L);

        PersonalExam personalExam = new PersonalExam();
        personalExam.setId("1");
        personalExam.setName("DDL requests");
        personalExam.setAnswers(List.of(answer1));
        personalExam.review();

        PersonalExam personalExam2 = new PersonalExam();
        personalExam2.setId("2");
        personalExam2.setName("DDL requests");
        personalExam2.setAnswers(List.of(answer2));

        when(personalExamConnector.getAllByAnswersTaskIdAndStatusIs(any(), any())).thenReturn(List.of(personalExam2));
        task.setQuestion("select * from products");

        List<PersonalExam> personalExams = personalExamService.updateTask(task);
        assertEquals(1, personalExams.size());
        assertEquals("2", personalExams.get(0).getId());
        assertEquals("select * from products", personalExams.get(0).getAnswerById(2L).getTask().getQuestion());
    }

    @Test
    public void shouldReturnPersonalExamWithGradesByExamAssigneeId() {
        PersonalExam personalExam = new PersonalExam();

        personalExam.setId("12345");
        personalExam.setName("DDL requests");

        Answer answer1 = new Answer();
        answer1.setValue("Select something 1");
        answer1.setStatus(AnswerStatus.GRADED);

        Answer answer2 = new Answer();
        answer2.setValue("Select something 2");
        answer2.setStatus(AnswerStatus.GRADED);

        List<Answer> answers = List.of(answer1, answer2);

        answers.forEach(el -> {
            el.setGrade(new Grade());
            el.getGrade().setValue(1.0);
            el.setTeacherGrade(new Grade());
            el.getTeacherGrade().setValue(2.0);
        });

        ExamAssignee examAssignee = new ExamAssignee();
        examAssignee.setExam(new Exam());
        examAssignee.setId(1L);

        personalExam.setExamAssigneeId(examAssignee.getId());

        personalExam.setAnswers(answers);

        when(personalExamConnector.getByExamAssigneeId(any())).thenReturn(List.of(personalExam));

        List<PersonalExam> personalExams = personalExamService.getByExamAssigneeId(examAssignee.getId());
        Assertions.assertEquals(1, personalExams.size());
        Assertions.assertEquals(2, personalExams.get(0).getAnswers().size());
        Assertions.assertEquals(2.0, personalExams.get(0).getSystemGrade());
        Assertions.assertEquals(4.0, personalExams.get(0).getTeacherGrade());
    }
}
