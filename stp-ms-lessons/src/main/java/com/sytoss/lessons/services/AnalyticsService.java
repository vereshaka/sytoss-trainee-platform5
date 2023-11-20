package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.AbsentDisciplineException;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.lessons.analytics.RatingModel;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.AnalyticsConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.convertors.AnalyticsConvertor;
import com.sytoss.lessons.dto.AnalyticsElementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class AnalyticsService extends AbstractService {

    private final AnalyticsConnector analyticsConnector;

    private final AnalyticsConvertor analyticsConvertor;

    private final ExamService examService;

    private final UserService userService;

    private final PersonalExamConnector personalExamConnector;

    private final DisciplineService disciplineService;

    public List<AnalyticsElementDTO> initializeAnalyticsElementDTOs(Long examId, Long disciplineId, List<Student> students) {
        List<AnalyticsElementDTO> analyticsElementDTOS = new ArrayList<>();
        for (Long studentId : students.stream().map(AbstractUser::getId).toList()) {
            AnalyticsElementDTO analyticsElementDTO = new AnalyticsElementDTO();
            analyticsElementDTO.setDisciplineId(disciplineId);
            analyticsElementDTO.setExamId(examId);
            analyticsElementDTO.setStudentId(studentId);
            analyticsElementDTO.setGrade(0.0);
            analyticsElementDTO.setTimeSpent(0L);
            analyticsElementDTO = analyticsConnector.save(analyticsElementDTO);
            analyticsElementDTOS.add(analyticsElementDTO);
        }
        return analyticsElementDTOS;
    }

    public AnalyticsElement updateAnalyticsElement(AnalyticsElement analyticsElement) {
        if (analyticsElement.getExamId() == null && analyticsElement.getExamAssigneeId() != null) {
            Exam exam = examService.getExamByExamAssignee(analyticsElement.getExamAssigneeId());
            if (exam != null) {
                analyticsElement.setExamId(exam.getId());
            }
        }
        AnalyticsElementDTO analyticsElementDTO = analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(analyticsElement.getDisciplineId(), analyticsElement.getExamId(), analyticsElement.getStudentId());
        if (analyticsElementDTO == null) {
            analyticsElementDTO = new AnalyticsElementDTO();
            analyticsConvertor.toDTO(analyticsElement, analyticsElementDTO);
        } else {
            if (analyticsElementDTO.getGrade() == null && analyticsElementDTO.getTimeSpent() == null && analyticsElementDTO.getPersonalExamId() == null) {
                analyticsElementDTO.setPersonalExamId(analyticsElement.getPersonalExamId());
                analyticsElementDTO.setGrade(analyticsElement.getGrade());
                analyticsElementDTO.setTimeSpent(analyticsElement.getTimeSpent());
            } else if ((analyticsElement.getGrade() >= analyticsElementDTO.getGrade() || analyticsElementDTO.getGrade() == 0) && analyticsElementDTO.getTimeSpent() >= analyticsElement.getTimeSpent() || analyticsElementDTO.getTimeSpent() == 0) {
                analyticsElementDTO.setGrade(analyticsElement.getGrade());
                analyticsElementDTO.setTimeSpent(analyticsElement.getTimeSpent());
                if (analyticsElementDTO.getPersonalExamId() == null || !analyticsElement.getPersonalExamId().equals(analyticsElementDTO.getPersonalExamId())) {
                    analyticsElementDTO.setPersonalExamId(analyticsElement.getPersonalExamId());
                }
            }
        }

        analyticsElementDTO = analyticsConnector.save(analyticsElementDTO);
        analyticsConvertor.fromDTO(analyticsElementDTO, analyticsElement);
        return analyticsElement;
    }

    public List<AnalyticsElement> migrate(Long disciplineId) {
        List<Student> students = userService.getStudents(disciplineId);
        for (Long studentId : students.stream().map(AbstractUser::getId).toList()) {
            analyticsConnector.deleteAnalyticsElementDTOByDisciplineIdAndStudentId(disciplineId, studentId);
        }

        List<Exam> exams = examService.getExamsByDiscipline(disciplineId);
        List<AnalyticsElement> analyticsElements = new ArrayList<>();
        List<PersonalExam> personalExams = new ArrayList<>();
        for (Exam exam : exams) {
            for (Student student : students) {
                for (ExamAssignee examAssigneeDTO : exam.getExamAssignees()) {
                    List<PersonalExam> personalExamsByExamAssignee = personalExamConnector
                            .getListOfPersonalExamByExamAssigneeId(examAssigneeDTO.getId()).stream().filter(personalExam -> personalExam.getStudent().getId().equals(student.getId())).toList();
                    for (PersonalExam personalExamByExamAssignee : personalExamsByExamAssignee) {
                        if (personalExams.isEmpty()) {
                            personalExams.add(personalExamByExamAssignee);
                        } else {
                            if (!personalExams.stream().map(el -> el.getStudent().getId()).toList().contains(personalExamByExamAssignee.getStudent().getId())) {
                                personalExams.add(personalExamByExamAssignee);
                            } else {
                                for (PersonalExam personalExamToCompare : personalExams) {
                                    if (Objects.equals(personalExamByExamAssignee.getStudent().getId(), personalExamToCompare.getStudent().getId())) {
                                        if (personalExamByExamAssignee.getSummaryGrade() >= personalExamToCompare.getSummaryGrade() && personalExamByExamAssignee.getSpentTime() <= personalExamToCompare.getSpentTime()) {
                                            int index = personalExams.indexOf(personalExamToCompare);
                                            personalExams.set(index, personalExamByExamAssignee);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (PersonalExam personalExam : personalExams) {
            AnalyticsElement analyticsElement = new AnalyticsElement();
            Exam examByExamAssignee = exams.stream().filter(examDTO -> examDTO.getExamAssignees().stream().map(ExamAssignee::getId).toList().contains(personalExam.getExamAssigneeId())).toList().get(0);
            analyticsElement.setExamId(examByExamAssignee.getId());
            analyticsElement.setDisciplineId(disciplineId);
            analyticsElement.setStudentId(personalExam.getStudent().getId());
            analyticsElement.setPersonalExamId(personalExam.getId());
            analyticsElement.setGrade(personalExam.getSummaryGrade());
            analyticsElement.setTimeSpent(personalExam.getSpentTime());
            analyticsElement.setStartDate(personalExam.getStartedDate());
            AnalyticsElementDTO analyticsElementDTO = new AnalyticsElementDTO();
            analyticsConvertor.toDTO(analyticsElement, analyticsElementDTO);
            analyticsConnector.save(analyticsElementDTO);
            analyticsElements.add(analyticsElement);
        }
        return analyticsElements;
    }

    public void deleteByExamId(Long examId) {
        analyticsConnector.deleteAllByExamId(examId);
    }

    public List<RatingModel> getAnalyticsElementsByDisciplineGroupExam(Long disciplineId, Long groupId, Long examId) {
        if (disciplineId == null) {
            throw new AbsentDisciplineException();
        }
        List<RatingModel> ratingModels;
        List<Long> students = new ArrayList<>();
        if (groupId != null) {
            students = userService.getStudentsOfGroup(groupId).stream().map(AbstractUser::getId).toList();
        }
        if (examId == null && groupId == null) {
            ratingModels = analyticsConnector.getStudentRatingsByDiscipline(disciplineId);
        } else if (groupId == null) {
            ratingModels = analyticsConnector.getStudentRatingsByDisciplineAndExamId(disciplineId, examId);
        } else if (examId == null) {
            ratingModels = analyticsConnector.getStudentRatingsByDisciplineAndGroupId(disciplineId, students);
        } else {
            ratingModels = analyticsConnector.getStudentRatingsByDisciplineAndGroupIdAndExamId(disciplineId, students, examId);
        }

        return ratingModels;
    }

    public Analytics getAnalyticByStudentId(Long disciplineId) {
        Long studentId = getCurrentUser().getId();
        if (disciplineId == null) {
            throw new AbsentDisciplineException();
        }
        return getAnalyticByStudentId(studentId);
    }

    public List<Analytics> getAnalyticsSummaryByDiscipline(Long disciplineId) {
        List<Analytics> analytics = new ArrayList<>();
        List<Student> students = userService.getStudents(disciplineId);
        for (Student student : students) {
            analytics.add(formAnalytics(disciplineId, student.getId()));
        }
        return analytics;
    }

    public List<Analytics> getAnalyticsSummaryByDisciplineAndGroup(Long disciplineId, Long groupId) {
        List<Analytics> analytics = new ArrayList<>();
        List<Student> students = userService.getStudentsOfGroup(groupId);
        for (Student student : students) {
            analytics.add(formAnalytics(disciplineId, student.getId()));
        }
        return analytics;
    }

    private Analytics formAnalytics(Long disciplineId, Long studentId) {
        RatingModel ratingModel = analyticsConnector.getStudentRatingsByDiscipline(disciplineId).stream().filter(model -> Objects.equals(model.getStudentId(), studentId)).toList().get(0);

        Analytics analytics = new Analytics();

        Discipline discipline = disciplineService.getById(disciplineId);
        analytics.setDiscipline(discipline);
        Student student = new Student();
        student.setId(ratingModel.getStudentId());
        analytics.setStudent(student);

        Grade averageGrade = new Grade();
        averageGrade.setGrade(ratingModel.getAvgGrade());
        averageGrade.setTimeSpent(ratingModel.getAvgTimeSpent());


        List<AnalyticsElementDTO> analyticsElementDTOS = analyticsConnector.getByDisciplineIdAndStudentId(disciplineId, studentId);
        double max = 0;
        double timeSpent = 0;
        for (AnalyticsElementDTO analyticsElementDTO : analyticsElementDTOS) {
            if (analyticsElementDTO.getGrade() > max && (analyticsElementDTO.getTimeSpent() < timeSpent || timeSpent == 0)) {
                max = analyticsElementDTO.getGrade();
                timeSpent = analyticsElementDTO.getTimeSpent();
            }
        }

        Grade maxGrade = new Grade();
        maxGrade.setGrade(max);
        maxGrade.setTimeSpent(timeSpent);
        StudentsGrade studentsGrade = new StudentsGrade();
        studentsGrade.setAverage(averageGrade);
        studentsGrade.setMax(maxGrade);
        analytics.setStudentsGrade(studentsGrade);

        List<ExamsElement> examsElementList = new ArrayList<>();
        for (AnalyticsElementDTO analyticsElementDTO : analyticsElementDTOS) {
            Exam exam = examService.getById(analyticsElementDTO.getExamId());
            PersonalExam personalExam = personalExamConnector.personalExamSummary(analyticsElementDTO.getPersonalExamId());
            ExamsElement examsElement = new ExamsElement();
            examsElement.setExam(exam);
            examsElement.setPersonalExam(personalExam);
            examsElementList.add(examsElement);
        }

        analytics.setExamsElementList(examsElementList);

        return analytics;
    }
}
