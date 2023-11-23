package com.sytoss.lessons.services;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.analytics.Rating;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.dto.AnalyticsAverageDTO;
import com.sytoss.lessons.dto.AnalyticsDTO;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService extends AbstractService {

    private final AnalyticsConnector analyticsConnector;

    private final UserConnector userConnector;

    private final GroupReferenceConnector groupReferenceConnector;

    private final ExamConnector examConnector;

    private final PersonalExamConnector personalExamConnector;

    private final DisciplineConnector disciplineConnector;

    public List<AnalyticsDTO> checkOrCreate(long examId, long disciplineId, List<Student> students) {
        List<AnalyticsDTO> analyticsElementDTOS = new ArrayList<>();
        for (Student student : students) {
            AnalyticsDTO analyticsDTO = analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(disciplineId, examId, student.getId());
            if (analyticsDTO == null) {
                analyticsDTO = new AnalyticsDTO();
                analyticsDTO.setDisciplineId(disciplineId);
                analyticsDTO.setExamId(examId);
                analyticsDTO.setStudentId(student.getId());
                analyticsDTO.setGrade(0.0);
                analyticsDTO.setTimeSpent(0L);
                analyticsDTO = analyticsConnector.save(analyticsDTO);
            }
            analyticsElementDTOS.add(analyticsDTO);
        }
        return analyticsElementDTOS;
    }

    public void migrateAll(){
        List<DisciplineDTO> disciplineDTOS = disciplineConnector.findAll();
        for(DisciplineDTO disciplineDTO : disciplineDTOS){
            migrate(disciplineDTO.getId());
        }
    }

    public List<Analytics> migrate(Long disciplineId) {
        List<GroupReferenceDTO> groupReferenceDTOS = groupReferenceConnector.findByDisciplineId(disciplineId);
        List<Student> students = new ArrayList<>();
        for (GroupReferenceDTO groupReferenceDTO : groupReferenceDTOS) {
            List<Student> studentsOfGroup = userConnector.getStudentOfGroup(groupReferenceDTO.getGroupId());
            studentsOfGroup.forEach(student -> {
                if (!students.stream().map(AbstractUser::getId).toList().contains(student.getId())) {
                    students.add(student);
                }
            });
        }

        for (Student student : students) {
            analyticsConnector.deleteAnalyticsDTOByDisciplineIdAndStudentId(disciplineId, student.getId());
        }
        List<ExamDTO> exams = examConnector.findByTopics_Discipline_Id(disciplineId);
        List<Analytics> analytics = new ArrayList<>();

        for (ExamDTO examDTO : exams) {
            checkOrCreate(disciplineId, examDTO.getId(), students);
        }

        List<PersonalExam> personalExams = personalExamConnector.getListOfPersonalExamByStudents(students);

        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        for (PersonalExam personalExam : personalExams) {
            Analytics analytic = new Analytics();
            analytic.setDiscipline(discipline);
            analytic.setExam(new Exam());
            analytic.getExam().setId(examConnector.findByExamAssignees_Id(personalExam.getExamAssigneeId()).getId());
            analytic.setStudent(personalExam.getStudent());
            analytic.setPersonalExam(personalExam);
            analytic.setGrade(new AnalyticGrade(personalExam.getSummaryGrade(), personalExam.getSpentTime()));
            analytic.setStartDate(personalExam.getStartedDate());
            updateAnalytic(analytic);
            analytics.add(analytic);
        }
        return analytics;
    }


    public List<AnalyticsDTO> checkOrCreate(long examId, long disciplineId, long groupId) {
        List<Student> students = userConnector.getStudentOfGroup(groupId);
        return checkOrCreate(examId, disciplineId, students);
    }

    public void updateAnalytic(Analytics analytics) {
        AnalyticsDTO dto = analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(
                analytics.getDiscipline().getId(),
                analytics.getExam().getId(),
                analytics.getStudent().getId());
        if (dto == null) {
            log.error("Now elements found");
            //TODO: yevgenyv: correct handling
            dto = new AnalyticsDTO();
            dto.setDisciplineId(analytics.getDiscipline().getId());
            dto.setExamId(analytics.getExam().getId());
            dto.setStudentId(analytics.getStudent().getId());
        }
        AnalyticGrade grade = analytics.getGrade();
        if (dto.getPersonalExamId() == null
                || grade.getGrade() > dto.getGrade()
                || (grade.getGrade() == dto.getGrade() && grade.getTimeSpent() < dto.getTimeSpent())) {
            dto.setPersonalExamId(analytics.getPersonalExam().getId());
            dto.setGrade(analytics.getGrade().getGrade());
            dto.setTimeSpent(analytics.getGrade().getTimeSpent());
            dto.setStartDate(analytics.getPersonalExam().getStartedDate());
        }
        analyticsConnector.save(dto);
    }

    public void deleteByExam(long examId){
        analyticsConnector.deleteAllByExamId(examId);
    }
    public void deleteByDiscipline(long disciplineId){
        analyticsConnector.deleteAllByDisciplineId(disciplineId);
    }

    public List<Rating> getAnalyticsElementsByDisciplineGroupExam(Long disciplineId, Long groupId, Long examId) {
        if (disciplineId == null) {
            throw new DisciplineNotFoundException(disciplineId);
        }
        List<AnalyticsAverageDTO> analyticsAverageDTOS;
        List<Long> students = new ArrayList<>();
        if (groupId != null) {
            students = userConnector.getStudentOfGroup(groupId).stream().map(AbstractUser::getId).toList();
        }
        if (examId == null && groupId == null) {
            analyticsAverageDTOS = analyticsConnector.getStudentRatingsByDiscipline(disciplineId);
        } else if (groupId == null) {
            analyticsAverageDTOS = analyticsConnector.getStudentRatingsByDisciplineAndExamId(disciplineId, examId);
        } else if (examId == null) {
            analyticsAverageDTOS = analyticsConnector.getStudentRatingsByDisciplineAndGroupId(disciplineId, students);
        } else {
            analyticsAverageDTOS = analyticsConnector.getStudentRatingsByDisciplineAndGroupIdAndExamId(disciplineId, students, examId);
        }

        List<Rating> ratings = new ArrayList<>();
        for (AnalyticsAverageDTO analyticsAverageDTO : analyticsAverageDTOS) {
            DisciplineDTO disciplineDTO = disciplineConnector.findById(disciplineId).orElse(null);

            Rating rating = new Rating();

            if (disciplineDTO != null) {
                Discipline discipline = new Discipline();
                discipline.setId(disciplineDTO.getId());
                discipline.setName(disciplineDTO.getName());
                rating.setDiscipline(discipline);
            } else {
                throw new DisciplineNotFoundException(disciplineId);
            }

            Student student = new Student();
            student.setId(analyticsAverageDTO.getStudentId());
            if (groupId != null) {
                Group group = new Group();
                group.setId(groupId);
                student.setPrimaryGroup(group);
            }
            rating.setStudent(student);

            AnalyticGrade analyticGrade = new AnalyticGrade();
            analyticGrade.setGrade(analyticsAverageDTO.getAvgGrade());
            analyticGrade.setTimeSpent(analyticsAverageDTO.getAvgTimeSpent());
            rating.setGrade(analyticGrade);
            rating.setRank(analyticsAverageDTO.getRank());
            ratings.add(rating);
        }
        return ratings;
    }
}
