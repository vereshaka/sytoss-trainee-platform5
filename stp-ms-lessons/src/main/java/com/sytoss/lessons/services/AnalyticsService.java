package com.sytoss.lessons.services;

import com.sytoss.domain.bom.analytics.AnaliticGrade;
import com.sytoss.domain.bom.analytics.Analytic;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.convertors.AnalyticsConvertor;
import com.sytoss.lessons.convertors.ExamAssigneeConvertor;
import com.sytoss.lessons.dto.AnalyticsDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private final AnalyticsConvertor analyticsConvertor;

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
    
    public List<Analytic> migrate(Long disciplineId) {
        List<GroupReferenceDTO> groupReferenceDTOS = groupReferenceConnector.findByDisciplineId(disciplineId);
        List<Student> students = new ArrayList<>();
        for(GroupReferenceDTO groupReferenceDTO : groupReferenceDTOS){
            List<Student> studentsOfGroup = userConnector.getStudentOfGroup(groupReferenceDTO.getGroupId());
            studentsOfGroup.forEach(student -> {
                if (students.stream().filter(item -> item.getId() != student.getId()).toList().isEmpty()) {
                    students.add(student);
                }
            });
        }

        for (Student student : students) {
            analyticsConnector.deleteAnalyticsDTOByDisciplineIdAndStudentId(disciplineId, student.getId());
        }
        List<ExamDTO> exams = examConnector.findByTopics_Discipline_Id(disciplineId);
        List<Analytic> analytics = new ArrayList<>();
        List<PersonalExam> personalExams = new ArrayList<>();
        for (ExamDTO exam : exams) {
            for (Student student : students) {
                for (ExamAssigneeDTO examAssigneeDTO : exam.getExamAssignees()) {
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
            Analytic analytic = new Analytic();
            ExamDTO examByExamAssignee = exams.stream().filter(examDTO -> examDTO.getExamAssignees().stream().map(ExamAssigneeDTO::getId).toList().contains(personalExam.getExamAssigneeId())).toList().get(0);
            Discipline discipline = new Discipline();
            discipline.setId(disciplineId);
            analytic.setDiscipline(discipline);
            Exam exam = new Exam();
            exam.setId(examByExamAssignee.getId());
            exam.setName(examByExamAssignee.getName());
            exam.setMaxGrade(examByExamAssignee.getMaxGrade());
            ExamAssigneeConvertor examAssigneeConvertor = new ExamAssigneeConvertor();
            for(ExamAssigneeDTO examAssigneeDTO : examByExamAssignee.getExamAssignees()){
                ExamAssignee examAssignee = new ExamAssignee();
                examAssigneeConvertor.fromDTO(examAssigneeDTO,examAssignee);
                exam.getExamAssignees().add(examAssignee);
            };
            analytic.setStudent(personalExam.getStudent());
            analytic.setPersonalExam(personalExam);
            AnaliticGrade analiticGrade = new AnaliticGrade();
            analiticGrade.setGrade(personalExam.getSummaryGrade());
            analiticGrade.setTimeSpent(personalExam.getSpentTime());
            analytic.setGrade(analiticGrade);
            AnalyticsDTO analyticsDTO = new AnalyticsDTO();
            analyticsConvertor.toDTO(analytic, analyticsDTO);
            analyticsConnector.save(analyticsDTO);
            analytics.add(analytic);
        }
        return analytics;
    }


    public List<AnalyticsDTO> checkOrCreate(long examId, long disciplineId, long groupId) {
        List<Student> students = userConnector.getStudentOfGroup(groupId);
        return checkOrCreate(examId, disciplineId, students);
    }

    public void updateAnalytic(Analytic analytic) {
        AnalyticsDTO dto = analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(
                analytic.getDiscipline().getId(),
                analytic.getExam().getId(),
                analytic.getStudent().getId());
        if (dto == null) {
            log.error("Now elements found");
            //TODO: yevgenyv: correct handling
            dto = new AnalyticsDTO();
        }
        AnaliticGrade grade = analytic.getGrade();
        if (dto.getPersonalExamId() == null
                || grade.getGrade() > dto.getGrade()
                || (grade.getGrade() == dto.getGrade() && grade.getTimeSpent() < dto.getTimeSpent())) {
            dto.setPersonalExamId(analytic.getPersonalExam().getId());
            dto.setGrade(analytic.getGrade().getGrade());
            dto.setTimeSpent(analytic.getGrade().getTimeSpent());
        }
        analyticsConnector.save(dto);
    }
}
