package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.convertors.TopicConvertor;
import com.sytoss.lessons.dto.ExamDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class ExamServiceTest extends StpUnitTest {

    @InjectMocks
    private ExamService examService;

    @Mock
    private ExamConnector examConnector;

    @Spy
    private ExamConvertor examConvertor = new ExamConvertor(new TopicConvertor(new DisciplineConvertor()));

    @Test
    public void shouldSaveExam() {
        Mockito.doAnswer((org.mockito.stubbing.Answer<ExamDTO>) invocation -> {
            final Object[] args = invocation.getArguments();
            ExamDTO result = (ExamDTO) args[0];
            result.setId(1L);
            return result;
        }).when(examConnector).save(any());

        Teacher teacher = createTeacher("John", "Johns");
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

        Exam result = examService.save(exam);
        Assertions.assertEquals(1L, result.getId());
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
