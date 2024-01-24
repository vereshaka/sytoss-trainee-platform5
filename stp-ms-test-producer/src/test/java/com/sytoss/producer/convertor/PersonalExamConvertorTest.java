package com.sytoss.producer.convertor;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.producer.interfaces.AnswerGenerator;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class PersonalExamConvertorTest extends StpUnitTest {

    @InjectMocks
    private PersonalExamConvertor personalExamConvertor;

    @Mock
    private AnswerGenerator answerGenerator;

    @Test
    public void shouldConvertFromExamConfiguration() {
        ExamConfiguration examConfiguration = createExamConfiguration();
        when(answerGenerator.generateAnswers(eq(1), anyList())).thenReturn(List.of(createAnswer()));

        PersonalExam personalExam = new PersonalExam();

        personalExamConvertor.fromExamConfiguration(examConfiguration, personalExam);

        assertEquals("New exam", personalExam.getName());
        assertEquals(10, personalExam.getMaxGrade());
        assertEquals(1, personalExam.getAnswers().size());
        assertEquals(2L, personalExam.getExamAssigneeId());
        assertEquals(1d, personalExam.getSumOfCoef());
        assertNotNull(personalExam.getDiscipline());
        assertNotNull(personalExam.getStudent());
        assertNotNull(personalExam.getRelevantFrom());
        assertNotNull(personalExam.getRelevantTo());
    }

    private Answer createAnswer() {
        Task task = new Task();
        task.setCoef(1d);
        Answer answer = new Answer();
        answer.setTask(task);

        return answer;
    }

    private ExamConfiguration createExamConfiguration() {
        ExamConfiguration examConfiguration = new ExamConfiguration();

        Discipline discipline = new Discipline();
        discipline.setId(1L);
        discipline.setName("SQL");

        Exam exam = new Exam();
        exam.setNumberOfTasks(1);
        exam.setMaxGrade(10);
        exam.setName("New exam");
        exam.setDiscipline(discipline);
        exam.setTasks(List.of(new Task()));

        ExamAssignee examAssignee = new ExamAssignee();
        examAssignee.setId(2L);
        examAssignee.setExam(exam);
        examAssignee.setRelevantFrom(new Date());
        examAssignee.setRelevantTo(new Date());

        Student student = new Student();

        examConfiguration.setExamAssignee(examAssignee);
        examConfiguration.setStudent(student);
        examConfiguration.setExam(exam);

        return examConfiguration;
    }

}
