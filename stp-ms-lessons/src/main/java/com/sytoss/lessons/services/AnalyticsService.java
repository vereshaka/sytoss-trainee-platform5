package com.sytoss.lessons.services;

import com.sytoss.domain.bom.analytics.*;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.dto.AnalyticsDTO;
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
        List<Analytic> analytics = new ArrayList<>();

        for (ExamDTO examDTO : exams) {
            checkOrCreate(disciplineId, examDTO.getId(), students);
        }

        List<PersonalExam> personalExams = personalExamConnector.getListOfPersonalExamByStudents(students);

        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        for (PersonalExam personalExam : personalExams) {
            Analytic analytic = new Analytic();
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

    public void updateAnalytic(Analytic analytic) {
        AnalyticsDTO dto = analyticsConnector.getByDisciplineIdAndExamIdAndStudentId(
                analytic.getDiscipline().getId(),
                analytic.getExam().getId(),
                analytic.getStudent().getId());
        if (dto == null) {
            log.error("Now elements found");
            //TODO: yevgenyv: correct handling
            dto = new AnalyticsDTO();
            dto.setDisciplineId(analytic.getDiscipline().getId());
            dto.setExamId(analytic.getExam().getId());
            dto.setStudentId(analytic.getStudent().getId());
        }
        AnalyticGrade grade = analytic.getGrade();
        if (dto.getPersonalExamId() == null
                || grade.getGrade() > dto.getGrade()
                || (grade.getGrade() == dto.getGrade() && grade.getTimeSpent() < dto.getTimeSpent())) {
            dto.setPersonalExamId(analytic.getPersonalExam().getId());
            dto.setGrade(analytic.getGrade().getGrade());
            dto.setTimeSpent(analytic.getGrade().getTimeSpent());
            dto.setStartDate(analytic.getPersonalExam().getStartedDate());
        }
        analyticsConnector.save(dto);
    }

    public void deleteByExam(long examId){
        analyticsConnector.deleteAllByExamId(examId);
    }
    public void deleteByDiscipline(long disciplineId){
        analyticsConnector.deleteAllByDisciplineId(disciplineId);
    }

    public AnalyticFull getStudentAnalyticsByLoggedStudent(Long disciplineId) {

        Long studentId = getCurrentUser().getId();

        return getStudentAnalyticsByStudentId(disciplineId, studentId);
    }

    public AnalyticFull getStudentAnalyticsByStudentId(Long disciplineId, Long studentId) {

        AnalyticFull analyticFull = createAnalyticFull(disciplineId, studentId);

        List<Test> tests = new ArrayList<>();
        List<AnalyticsDTO> analyticsDTOS = analyticsConnector.getByDisciplineIdAndStudentId(disciplineId, studentId);

        for (AnalyticsDTO analyticsDTO : analyticsDTOS) {
            Test test = new Test();
            test.setExam(getExam(analyticsDTO));
            test.setPersonalExam(getPersonalExam(analyticsDTO));
            tests.add(test);
        }

        analyticFull.setTests(tests);

        return analyticFull;
    }

    private Exam getExam(AnalyticsDTO analyticsDTO) {
        return analyticsConnector.getExamInfo(analyticsDTO.getExamId());
    }

    private PersonalExam getPersonalExam(AnalyticsDTO analyticsDTO) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId(analyticsDTO.getPersonalExamId());
        personalExam.setSpentTime(analyticsDTO.getTimeSpent());
        personalExam.setMaxGrade(analyticsDTO.getGrade());
        personalExam.setStartedDate(analyticsDTO.getStartDate());
        return personalExam;
    }

    private AnalyticFull createAnalyticFull(Long disciplineId, Long studentId) {
        AnalyticFull analyticFull = new AnalyticFull();

        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);

        Student student = new Student();
        student.setId(studentId);

        analyticFull.setDiscipline(discipline);
        analyticFull.setStudent(student);

        AnalyticGrade averageGrade = analyticsConnector.getAverageGrade(disciplineId, studentId);
        AnalyticGrade maxGrade = analyticsConnector.getMaxGrade(disciplineId, studentId);

        SummaryGrade summaryGrade = new SummaryGrade();
        summaryGrade.setAverage(averageGrade);
        summaryGrade.setMax(maxGrade);
        analyticFull.setStudentGrade(summaryGrade);

        return analyticFull;
    }

}
