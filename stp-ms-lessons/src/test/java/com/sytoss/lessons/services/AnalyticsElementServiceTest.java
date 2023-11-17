package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.AnalyticsElement;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.AnalyticsConnector;
import com.sytoss.lessons.convertors.AnalyticsConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.AnalyticsElementDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AnalyticsElementServiceTest extends StpUnitTest {

    @Mock
    private AnalyticsConnector analyticsConnector;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Spy
    private AnalyticsConvertor analyticsConvertor = new AnalyticsConvertor();

    @Mock
    private UserService userService;

    @Mock
    private ExamConnector examConnector;

    @Mock
    private PersonalExamConnector personalExamConnector;

    @Mock
    private GroupService groupService;

    @Test
    void initializeAnalyticsElementDTOs() {
        Student student = new Student();
        student.setId(1L);

        Student student2 = new Student();
        student2.setId(2L);

        Discipline discipline = new Discipline();
        discipline.setId(1L);

        Exam exam = new Exam();
        exam.setId(1L);
        exam.setDiscipline(discipline);

        List<AnalyticsElementDTO> analyticsElementDTOS = analyticsService.initializeAnalyticsElementDTOs(exam.getId(), discipline.getId(), List.of(student, student2));

        Assertions.assertEquals(2, analyticsElementDTOS.size());
    }

    @Test
    void updateAnalyticsElementWithEqualTimeAndBetterGrade() {
        AnalyticsElement analyticsElement = new AnalyticsElement();
        analyticsElement.setDisciplineId(1L);
        analyticsElement.setExamId(1L);
        analyticsElement.setStudentId(1L);
        analyticsElement.setPersonalExamId("1");
        analyticsElement.setGrade(10.0);
        analyticsElement.setTimeSpent(2L);

        AnalyticsElementDTO analyticsElementDTO = new AnalyticsElementDTO();
        analyticsElementDTO.setDisciplineId(analyticsElement.getDisciplineId());
        analyticsElementDTO.setExamId(analyticsElement.getExamId());
        analyticsElementDTO.setStudentId(analyticsElement.getStudentId());
        analyticsElementDTO.setPersonalExamId(analyticsElementDTO.getPersonalExamId());
        analyticsElementDTO.setGrade(analyticsElement.getGrade());
        analyticsElementDTO.setTimeSpent(analyticsElement.getTimeSpent());
        when(analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(analyticsElement.getDisciplineId(), analyticsElement.getExamId(), analyticsElement.getStudentId())).thenReturn(analyticsElementDTO);

        analyticsElement.setGrade(12.0);
        analyticsElementDTO.setGrade(analyticsElement.getGrade());
        when(analyticsConnector.save(analyticsElementDTO)).thenReturn(analyticsElementDTO);

        AnalyticsElement newAnalyticsElement = analyticsService.updateAnalyticsElement(analyticsElement);
        Assertions.assertEquals(12.0, newAnalyticsElement.getGrade());
        Assertions.assertEquals(2L, newAnalyticsElement.getTimeSpent());
    }

    @Test
    void updateAnalyticsElementWithBiggerTimeAndBetterGrade() {
        AnalyticsElement analyticsElement = new AnalyticsElement();
        analyticsElement.setDisciplineId(1L);
        analyticsElement.setExamId(1L);
        analyticsElement.setStudentId(1L);
        analyticsElement.setPersonalExamId("1");
        analyticsElement.setGrade(10.0);
        analyticsElement.setTimeSpent(2L);

        AnalyticsElementDTO analyticsElementDTO = new AnalyticsElementDTO();
        analyticsElementDTO.setDisciplineId(analyticsElement.getDisciplineId());
        analyticsElementDTO.setExamId(analyticsElement.getExamId());
        analyticsElementDTO.setStudentId(analyticsElement.getStudentId());
        analyticsElementDTO.setPersonalExamId(analyticsElementDTO.getPersonalExamId());
        analyticsElementDTO.setGrade(analyticsElement.getGrade());
        analyticsElementDTO.setTimeSpent(analyticsElement.getTimeSpent());
        when(analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(analyticsElement.getDisciplineId(), analyticsElement.getExamId(), analyticsElement.getStudentId())).thenReturn(analyticsElementDTO);

        analyticsElement.setGrade(12.0);
        analyticsElement.setTimeSpent(3L);
        analyticsElementDTO.setGrade(analyticsElement.getGrade());
        when(analyticsConnector.save(analyticsElementDTO)).thenReturn(analyticsElementDTO);

        AnalyticsElement newAnalyticsElement = analyticsService.updateAnalyticsElement(analyticsElement);
        Assertions.assertEquals(12.0, newAnalyticsElement.getGrade());
        Assertions.assertEquals(2L, newAnalyticsElement.getTimeSpent());
    }


    @Test
    void updateAnalyticsElementWithLessTimeAndBetterGrade() {
        AnalyticsElement analyticsElement = new AnalyticsElement();
        analyticsElement.setDisciplineId(1L);
        analyticsElement.setExamId(1L);
        analyticsElement.setStudentId(1L);
        analyticsElement.setPersonalExamId("1");
        analyticsElement.setGrade(10.0);
        analyticsElement.setTimeSpent(2L);

        AnalyticsElementDTO analyticsElementDTO = new AnalyticsElementDTO();
        analyticsElementDTO.setDisciplineId(analyticsElement.getDisciplineId());
        analyticsElementDTO.setExamId(analyticsElement.getExamId());
        analyticsElementDTO.setStudentId(analyticsElement.getStudentId());
        analyticsElementDTO.setPersonalExamId(analyticsElementDTO.getPersonalExamId());
        analyticsElementDTO.setGrade(analyticsElement.getGrade());
        analyticsElementDTO.setTimeSpent(analyticsElement.getTimeSpent());
        when(analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(analyticsElement.getDisciplineId(), analyticsElement.getExamId(), analyticsElement.getStudentId())).thenReturn(analyticsElementDTO);

        analyticsElement.setGrade(12.0);
        analyticsElement.setTimeSpent(1L);
        analyticsElementDTO.setGrade(analyticsElement.getGrade());
        analyticsElementDTO.setTimeSpent(analyticsElement.getTimeSpent());
        when(analyticsConnector.save(analyticsElementDTO)).thenReturn(analyticsElementDTO);

        AnalyticsElement newAnalyticsElement = analyticsService.updateAnalyticsElement(analyticsElement);
        Assertions.assertEquals(12.0, newAnalyticsElement.getGrade());
        Assertions.assertEquals(1L, newAnalyticsElement.getTimeSpent());
    }

    @Test
    void updateAnalyticsElementWithAnotherPersonalExamIdAndEqualValues() {
        AnalyticsElement analyticsElement = new AnalyticsElement();
        analyticsElement.setDisciplineId(1L);
        analyticsElement.setExamId(1L);
        analyticsElement.setStudentId(1L);
        analyticsElement.setPersonalExamId("1");
        analyticsElement.setGrade(10.0);
        analyticsElement.setTimeSpent(2L);

        AnalyticsElementDTO analyticsElementDTO = new AnalyticsElementDTO();
        analyticsElementDTO.setDisciplineId(analyticsElement.getDisciplineId());
        analyticsElementDTO.setExamId(analyticsElement.getExamId());
        analyticsElementDTO.setStudentId(analyticsElement.getStudentId());
        analyticsElementDTO.setPersonalExamId(analyticsElementDTO.getPersonalExamId());
        analyticsElementDTO.setGrade(analyticsElement.getGrade());
        analyticsElementDTO.setTimeSpent(analyticsElement.getTimeSpent());
        when(analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(analyticsElement.getDisciplineId(), analyticsElement.getExamId(), analyticsElement.getStudentId())).thenReturn(analyticsElementDTO);

        analyticsElement.setPersonalExamId("2");
        analyticsElementDTO.setGrade(analyticsElement.getGrade());
        analyticsElementDTO.setTimeSpent(analyticsElement.getTimeSpent());
        when(analyticsConnector.save(analyticsElementDTO)).thenReturn(analyticsElementDTO);

        AnalyticsElement newAnalyticsElement = analyticsService.updateAnalyticsElement(analyticsElement);
        Assertions.assertEquals(10.0, newAnalyticsElement.getGrade());
        Assertions.assertEquals(2L, newAnalyticsElement.getTimeSpent());
    }

    @Test
    void migrate() {
        Student student = new Student();
        student.setId(1L);

        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(1L);

        Discipline discipline = new Discipline();
        discipline.setId(disciplineDTO.getId());

        Group group = new Group();
        group.setId(1L);
        group.setDiscipline(discipline);
        student.setPrimaryGroup(group);
        when(userService.getStudents(1L)).thenReturn(List.of(student));

        ExamDTO examDTO = new ExamDTO();
        examDTO.setId(1L);
        examDTO.setDiscipline(disciplineDTO);
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeDTO.setExam(examDTO);
        examAssigneeDTO.setId(1L);
        examDTO.setExamAssignees(List.of(examAssigneeDTO));

        when(examConnector.findByTopics_Discipline_Id(disciplineDTO.getId())).thenReturn(List.of(examDTO));

        PersonalExam personalExam = new PersonalExam();
        personalExam.setDiscipline(discipline);
        personalExam.setId("1");
        personalExam.setStudent(student);
        personalExam.summary();
        personalExam.review();
        personalExam.setSummaryGrade(10.0);
        ExamAssignee examAssignee = new ExamAssignee();
        Exam exam = new Exam();
        exam.setId(1L);
        examAssignee.setExam(exam);
        examAssignee.setId(1L);
        personalExam.setExamAssigneeId(examAssignee.getId());

        when(personalExamConnector.getListOfPersonalExamByExamAssigneeId(examAssignee.getId())).thenReturn(List.of(personalExam));

        AnalyticsElementDTO analyticsElementDTO = new AnalyticsElementDTO();
        analyticsElementDTO.setDisciplineId(discipline.getId());
        analyticsElementDTO.setExamId(examDTO.getId());
        analyticsElementDTO.setStudentId(student.getId());
        analyticsElementDTO.setPersonalExamId(personalExam.getId());
        analyticsElementDTO.setGrade(personalExam.getSystemGrade());
        analyticsElementDTO.setTimeSpent(1L);

        List<AnalyticsElement> analyticsElements = analyticsService.migrate(discipline.getId());

        assertEquals(1, analyticsElements.size());
        assertEquals(discipline.getId(), analyticsElements.get(0).getDisciplineId());
        assertEquals(exam.getId(), analyticsElements.get(0).getExamId());
        assertEquals(student.getId(), analyticsElements.get(0).getStudentId());
        assertEquals(10.0, analyticsElements.get(0).getGrade());
    }
}
