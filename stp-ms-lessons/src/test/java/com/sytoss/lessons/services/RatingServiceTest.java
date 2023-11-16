package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Rating;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.RatingConnector;
import com.sytoss.lessons.convertors.RatingConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.RatingDTO;
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

public class RatingServiceTest extends StpUnitTest {

    @Mock
    private RatingConnector ratingConnector;

    @InjectMocks
    private RatingService ratingService;

    @Spy
    private RatingConvertor ratingConvertor = new RatingConvertor();

    @Mock
    private UserService userService;

    @Mock
    private ExamConnector examConnector;

    @Mock
    private PersonalExamConnector personalExamConnector;

    @Mock
    private GroupService groupService;

    @Test
    void initializeRatingsDTOs() {
        Student student = new Student();
        student.setId(1L);

        Student student2 = new Student();
        student2.setId(2L);

        Discipline discipline = new Discipline();
        discipline.setId(1L);

        Exam exam = new Exam();
        exam.setId(1L);
        exam.setDiscipline(discipline);

        List<RatingDTO> ratingDTOS = ratingService.initializeRatingsDTOs(exam.getId(), discipline.getId(), List.of(student, student2));

        Assertions.assertEquals(2, ratingDTOS.size());
    }

    @Test
    void updateRatingWithEqualTimeAndBetterGrade() {
        Rating rating = new Rating();
        rating.setDisciplineId(1L);
        rating.setExamId(1L);
        rating.setStudentId(1L);
        rating.setPersonalExamId("1");
        rating.setGrade(10.0);
        rating.setTimeSpent(2L);

        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setDisciplineId(rating.getDisciplineId());
        ratingDTO.setExamId(rating.getExamId());
        ratingDTO.setStudentId(rating.getStudentId());
        ratingDTO.setPersonalExamId(ratingDTO.getPersonalExamId());
        ratingDTO.setGrade(rating.getGrade());
        ratingDTO.setTimeSpent(rating.getTimeSpent());
        when(ratingConnector.getByDisciplineIdAndExamIdAndStudentId(rating.getDisciplineId(), rating.getExamId(), rating.getStudentId())).thenReturn(ratingDTO);

        rating.setGrade(12.0);
        ratingDTO.setGrade(rating.getGrade());
        when(ratingConnector.save(ratingDTO)).thenReturn(ratingDTO);

        Rating newRating = ratingService.updateRating(rating);
        Assertions.assertEquals(12.0, newRating.getGrade());
        Assertions.assertEquals(2L, newRating.getTimeSpent());
    }

    @Test
    void updateRatingWithBiggerTimeAndBetterGrade() {
        Rating rating = new Rating();
        rating.setDisciplineId(1L);
        rating.setExamId(1L);
        rating.setStudentId(1L);
        rating.setPersonalExamId("1");
        rating.setGrade(10.0);
        rating.setTimeSpent(2L);

        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setDisciplineId(rating.getDisciplineId());
        ratingDTO.setExamId(rating.getExamId());
        ratingDTO.setStudentId(rating.getStudentId());
        ratingDTO.setPersonalExamId(ratingDTO.getPersonalExamId());
        ratingDTO.setGrade(rating.getGrade());
        ratingDTO.setTimeSpent(rating.getTimeSpent());
        when(ratingConnector.getByDisciplineIdAndExamIdAndStudentId(rating.getDisciplineId(), rating.getExamId(), rating.getStudentId())).thenReturn(ratingDTO);

        rating.setGrade(12.0);
        rating.setTimeSpent(3L);
        ratingDTO.setGrade(rating.getGrade());
        when(ratingConnector.save(ratingDTO)).thenReturn(ratingDTO);

        Rating newRating = ratingService.updateRating(rating);
        Assertions.assertEquals(12.0, newRating.getGrade());
        Assertions.assertEquals(2L, newRating.getTimeSpent());
    }


    @Test
    void updateRatingWithLessTimeAndBetterGrade() {
        Rating rating = new Rating();
        rating.setDisciplineId(1L);
        rating.setExamId(1L);
        rating.setStudentId(1L);
        rating.setPersonalExamId("1");
        rating.setGrade(10.0);
        rating.setTimeSpent(2L);

        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setDisciplineId(rating.getDisciplineId());
        ratingDTO.setExamId(rating.getExamId());
        ratingDTO.setStudentId(rating.getStudentId());
        ratingDTO.setPersonalExamId(ratingDTO.getPersonalExamId());
        ratingDTO.setGrade(rating.getGrade());
        ratingDTO.setTimeSpent(rating.getTimeSpent());
        when(ratingConnector.getByDisciplineIdAndExamIdAndStudentId(rating.getDisciplineId(), rating.getExamId(), rating.getStudentId())).thenReturn(ratingDTO);

        rating.setGrade(12.0);
        rating.setTimeSpent(1L);
        ratingDTO.setGrade(rating.getGrade());
        ratingDTO.setTimeSpent(rating.getTimeSpent());
        when(ratingConnector.save(ratingDTO)).thenReturn(ratingDTO);

        Rating newRating = ratingService.updateRating(rating);
        Assertions.assertEquals(12.0, newRating.getGrade());
        Assertions.assertEquals(1L, newRating.getTimeSpent());
    }

    @Test
    void updateRatingWithAnotherPersonalExamIdAndEqualValues() {
        Rating rating = new Rating();
        rating.setDisciplineId(1L);
        rating.setExamId(1L);
        rating.setStudentId(1L);
        rating.setPersonalExamId("1");
        rating.setGrade(10.0);
        rating.setTimeSpent(2L);

        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setDisciplineId(rating.getDisciplineId());
        ratingDTO.setExamId(rating.getExamId());
        ratingDTO.setStudentId(rating.getStudentId());
        ratingDTO.setPersonalExamId(ratingDTO.getPersonalExamId());
        ratingDTO.setGrade(rating.getGrade());
        ratingDTO.setTimeSpent(rating.getTimeSpent());
        when(ratingConnector.getByDisciplineIdAndExamIdAndStudentId(rating.getDisciplineId(), rating.getExamId(), rating.getStudentId())).thenReturn(ratingDTO);

        rating.setPersonalExamId("2");
        ratingDTO.setGrade(rating.getGrade());
        ratingDTO.setTimeSpent(rating.getTimeSpent());
        when(ratingConnector.save(ratingDTO)).thenReturn(ratingDTO);

        Rating newRating = ratingService.updateRating(rating);
        Assertions.assertEquals(10.0, newRating.getGrade());
        Assertions.assertEquals(2L, newRating.getTimeSpent());
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

        when(personalExamConnector.getListOfPersonalExamByExamAssigneeIdAndStudentId(examAssignee.getId(), student.getId())).thenReturn(List.of(personalExam));

        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setDisciplineId(discipline.getId());
        ratingDTO.setExamId(examDTO.getId());
        ratingDTO.setStudentId(student.getId());
        ratingDTO.setPersonalExamId(personalExam.getId());
        ratingDTO.setGrade(personalExam.getSystemGrade());
        ratingDTO.setTimeSpent(1L);

        List<Rating> ratings = ratingService.migrate(discipline.getId());

        assertEquals(1, ratings.size());
        assertEquals(discipline.getId(), ratings.get(0).getDisciplineId());
        assertEquals(exam.getId(), ratings.get(0).getExamId());
        assertEquals(student.getId(), ratings.get(0).getStudentId());
        assertEquals(10.0, ratings.get(0).getGrade());
    }
}
