package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.AnalyticsElement;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.AnalyticsConnector;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.convertors.AnalyticsConvertor;
import com.sytoss.lessons.dto.AnalyticsElementDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
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

    private final ExamConnector examConnector;

    private final UserService userService;

    private final PersonalExamConnector personalExamConnector;

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
            ExamDTO exam = examConnector.findByExamAssignees_Id(analyticsElement.getExamAssigneeId());
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

        List<ExamDTO> exams = examConnector.findByTopics_Discipline_Id(disciplineId);
        List<AnalyticsElement> analyticsElements = new ArrayList<>();
        List<PersonalExam> personalExams = new ArrayList<>();
        for (ExamDTO exam : exams) {
            for (Student student : students) {
                for (ExamAssigneeDTO examAssigneeDTO : exam.getExamAssignees()) {
                    List<PersonalExam> personalExamsByExamAssignee = personalExamConnector
                            .getListOfPersonalExamByExamAssigneeIdAndStudentId(examAssigneeDTO.getId(), student.getId());
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
            ExamDTO examDTOByExamAssignee = exams.stream().filter(examDTO -> examDTO.getExamAssignees().stream().map(ExamAssigneeDTO::getId).toList().contains(personalExam.getExamAssigneeId())).toList().get(0);
            analyticsElement.setExamId(examDTOByExamAssignee.getId());
            analyticsElement.setDisciplineId(disciplineId);
            analyticsElement.setStudentId(personalExam.getStudent().getId());
            analyticsElement.setPersonalExamId(personalExam.getId());
            analyticsElement.setGrade(personalExam.getSummaryGrade());
            analyticsElement.setTimeSpent(personalExam.getSpentTime());
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
}
