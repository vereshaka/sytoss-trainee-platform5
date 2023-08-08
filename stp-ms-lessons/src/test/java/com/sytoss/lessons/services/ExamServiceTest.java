package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.convertors.*;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.ExamDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.TopicDTO;
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

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExamServiceTest extends StpUnitTest {

    @InjectMocks
    private ExamService examService;

    @Mock
    private ExamConnector examConnector;

    @Spy
    private ExamConvertor examConvertor = new ExamConvertor(new TopicConvertor(new DisciplineConvertor()),
            new TaskConvertor(new TaskDomainConvertor(new DisciplineConvertor()), new TaskConditionConvertor()));

    @Test
    public void shouldSaveExam() {
        Mockito.doAnswer((org.mockito.stubbing.Answer<ExamDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            ExamDTO result = (ExamDTO) args[0];
            result.setId(1L);
            return result;
        }).when(examConnector).save(any());

        Teacher teacher = createTeacher("John", "Johns");

        Jwt principal = Jwt.withTokenValue("123").header("myHeader", "value").claim("user", teacher).build();
        Object credential = null;
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, credential);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Discipline discipline = createDiscipline("SQL", teacher);
        Group group = createGroup("AT-21-2", discipline);

        Exam exam = new Exam();
        exam.setName("Exam");
        exam.setRelevantFrom(new Date());
        exam.setRelevantTo(new Date());
        exam.setGroup(group);
        exam.setDuration(15);
        exam.setNumberOfTasks(10);
        exam.setTopics(List.of(new Topic(), new Topic()));
        Task task = new Task();
        task.setTaskDomain(new TaskDomain());
        exam.setTasks(List.of(task));

        Exam result = examService.save(exam);
        Assertions.assertEquals(1L, result.getId());
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
        exam.setRelevantFrom(new Date());
        exam.setRelevantTo(new Date());
        exam.setGroupId(1L);
        exam.setDuration(15);
        exam.setNumberOfTasks(10);
        exam.setTopics(List.of(topicDTO));
        exam.setTeacherId(1L);

        when(examConnector.findByTeacherId(1L)).thenReturn(List.of(exam));

        List<Exam> result = examService.findExams();
        Assertions.assertEquals(1, result.size());
    }

    private Group createGroup(String groupName, Discipline discipline) {
        Group group = new Group();
        group.setName(groupName);
        group.setDiscipline(discipline);
        return group;
    }

    private Discipline createDiscipline(String disciplineName, Teacher teacher) {
        Discipline discipline = new Discipline();
        discipline.setName(disciplineName);
        discipline.setTeacher(teacher);
        return discipline;
    }

    private Teacher createTeacher(String firstName, String lastName) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        return teacher;
    }
}
