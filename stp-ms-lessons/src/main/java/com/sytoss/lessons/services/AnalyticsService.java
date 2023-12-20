package com.sytoss.lessons.services;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.analytics.Rating;
import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.PersonalExamByStudentsModel;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.*;
import com.sytoss.lessons.controllers.viewModel.*;
import com.sytoss.lessons.convertors.SummaryGradeByExamConvertor;
import com.sytoss.lessons.convertors.SummaryGradeConvertor;
import com.sytoss.lessons.dto.*;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    private final SummaryGradeConvertor summaryGradeConvertor;

    private final SummaryGradeByExamConvertor summaryGradeByExamConvertor;

    private final ExamAssigneeConnector examAssigneeConnector;

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

    public void migrateAll() {
        List<DisciplineDTO> disciplineDTOS = disciplineConnector.findAll();
        for (DisciplineDTO disciplineDTO : disciplineDTOS) {
            DisciplineDTO dto = disciplineConnector.findById(disciplineDTO.getId()).orElse(null);
            if (dto != null) {
                migrate(disciplineDTO.getId());
                log.info("Migration of discipline #" + disciplineDTO.getId() + " finished");
            } else {
                log.warn("Migration of discipline #" + disciplineDTO.getId() + " not started. Is ABSENT!");
            }

        }
    }

    public void migrate(Long disciplineId) {
        try {
            log.info("Migration of discipline #" + disciplineId + " started");
            log.info("Migration of discipline #" + disciplineId + ". Old analytics are started clearing");
            List<GroupReferenceDTO> groupReferenceDTOS = groupReferenceConnector.findByDisciplineId(disciplineId);
            for (GroupReferenceDTO groupReferenceDTO : groupReferenceDTOS) {
                try {
                    List<Student> studentsOfGroup = userConnector.getStudentOfGroup(groupReferenceDTO.getGroupId());
                    studentsOfGroup.forEach(student -> analyticsConnector.deleteAnalyticsDTOByDisciplineIdAndStudentId(disciplineId, student.getId()));
                } catch (Exception e) {
                    log.error("Fail to load group info. GroupId: " + groupReferenceDTO.getGroupId(), e);
                }
            }
            log.info("Migration of discipline #" + disciplineId + ". Old analytics for these students clear");

            log.info("Migration of discipline #" + disciplineId + ". Loading of students started");
            groupReferenceDTOS = groupReferenceConnector.findByDisciplineIdAndIsExcluded(disciplineId, false);
            List<Student> students = new ArrayList<>();
            for (GroupReferenceDTO groupReferenceDTO : groupReferenceDTOS) {
                try {
                    List<Student> studentsOfGroup = userConnector.getStudentOfGroup(groupReferenceDTO.getGroupId());
                    studentsOfGroup.forEach(student -> {
                        if (!students.stream().map(AbstractUser::getId).toList().contains(student.getId())) {
                            students.add(student);
                        }
                    });
                } catch (Exception e) {
                    log.error("Fail to load group info. GroupId: " + groupReferenceDTO.getGroupId(), e);
                }
            }
            log.info("Migration of discipline #" + disciplineId + ". Loading of students finished");

            List<ExamDTO> exams = examConnector.findByDiscipline_Id(disciplineId);

            List<Long> examAssigneesIds = new ArrayList<>();
            for (ExamDTO examDTO : exams) {
                checkOrCreate(examDTO.getId(), disciplineId, students);
                examAssigneeConnector.getAllByExam_Id(examDTO.getId()).forEach(examAssigneeDTO -> {
                    if (!examAssigneesIds.contains(examAssigneeDTO.getId())) {
                        examAssigneesIds.add(examAssigneeDTO.getId());
                    }
                });
            }
            log.info("Migration of discipline #" + disciplineId + ". Initial analytics data inserted");

            PersonalExamByStudentsModel personalExamByStudentsModel = new PersonalExamByStudentsModel();
            personalExamByStudentsModel.setDisciplineId(disciplineId);
            personalExamByStudentsModel.setExamAssignees(examAssigneesIds);
            personalExamByStudentsModel.setStudents(students);
            List<PersonalExam> personalExams = personalExamConnector.getListOfPersonalExamByStudents(personalExamByStudentsModel);

            Discipline discipline = new Discipline();
            discipline.setId(disciplineId);
            for (PersonalExam personalExam : personalExams) {
                Analytics analytic = new Analytics();
                analytic.setDiscipline(discipline);
                analytic.setExam(new Exam());
                ExamDTO examDto = examConnector.findByExamAssignees_Id(personalExam.getExamAssigneeId());
                if (examDto == null) {
                    log.warn("Exam not found. Personal exam: " + personalExam.getExamAssigneeId());
                    continue;
                }
                analytic.getExam().setId(examConnector.findByExamAssignees_Id(personalExam.getExamAssigneeId()).getId());
                analytic.setStudent(personalExam.getStudent());
                analytic.setPersonalExam(personalExam);
                analytic.setGrade(new AnalyticGrade(personalExam.getSummaryGrade(), personalExam.getSpentTime() == null ? 0 : personalExam.getSpentTime()));
                analytic.setStartDate(personalExam.getStartedDate() == null ? personalExam.getRelevantFrom() : personalExam.getStartedDate());
                if (personalExam.getStatus().equals(PersonalExamStatus.REVIEWED)) {
                    personalExam.summary();
                    updateAnalytic(analytic);
                }
            }
            log.info("Migration of discipline #" + disciplineId + ". Personal Exam data updated");
            log.info("Migration of discipline #" + disciplineId + " finished");
        } catch (Exception e) {
            log.error("Migration of discipline #" + disciplineId + " failed", e);
        }
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
            dto = new AnalyticsDTO();
            dto.setDisciplineId(analytics.getDiscipline().getId());
            dto.setExamId(analytics.getExam().getId());
            dto.setStudentId(analytics.getStudent().getId());
        }
        AnalyticGrade grade = analytics.getGrade();
        if (dto.getPersonalExamId() == null
                || (dto.getPersonalExamId().equals(analytics.getPersonalExam().getId()))
                || grade.getGrade() > dto.getGrade()
                || (grade.getGrade() == dto.getGrade() && grade.getTimeSpent() < dto.getTimeSpent())) {
            dto.setPersonalExamId(analytics.getPersonalExam().getId());
            dto.setGrade(analytics.getGrade().getGrade());
            dto.setTimeSpent(analytics.getGrade().getTimeSpent());
            dto.setStartDate(analytics.getStartDate());
        }
        analyticsConnector.save(dto);
    }

    public void deleteByExam(long examId) {
        analyticsConnector.deleteAllByExamId(examId);
    }

    public void deleteByDiscipline(long disciplineId) {
        analyticsConnector.deleteAllByDisciplineId(disciplineId);
    }

    public List<Rating> getAnalyticsElementsByDisciplineGroupExam(Long disciplineId, Long groupId, Long examId, String gradeType) {
        if (disciplineId == null) {
            throw new DisciplineNotFoundException(disciplineId);
        }
        List<AbstractAnalyticsDTO> analyticsDTOS = new ArrayList<>();
        List<Long> students = new ArrayList<>();
        if (groupId != null) {
            students = userConnector.getStudentOfGroup(groupId).stream().map(AbstractUser::getId).toList();
        }
        if (Objects.equals(gradeType, "summary")) {
            List<AnalyticsSummaryDTO> analyticsSummaryDTOS;
            if (examId == null && groupId == null) {
                analyticsSummaryDTOS = analyticsConnector.getSumStudentRatingsByDiscipline(disciplineId);
            } else if (groupId == null) {
                analyticsSummaryDTOS = analyticsConnector.getSumStudentRatingsByDisciplineAndExamId(disciplineId, examId);
            } else if (examId == null) {
                analyticsSummaryDTOS = analyticsConnector.getSumStudentRatingsByDisciplineAndGroupId(disciplineId, students);
            } else {
                analyticsSummaryDTOS = analyticsConnector.getSumStudentRatingsByDisciplineAndGroupIdAndExamId(disciplineId, students, examId);
            }
            analyticsDTOS.addAll(analyticsSummaryDTOS);
        } else {
            List<AnalyticsAverageDTO> analyticsAverageDTOS;
            if (examId == null && groupId == null) {
                analyticsAverageDTOS = analyticsConnector.getAvgStudentRatingsByDiscipline(disciplineId);
            } else if (groupId == null) {
                analyticsAverageDTOS = analyticsConnector.getAvgStudentRatingsByDisciplineAndExamId(disciplineId, examId);
            } else if (examId == null) {
                analyticsAverageDTOS = analyticsConnector.getAvgStudentRatingsByDisciplineAndGroupId(disciplineId, students);
            } else {
                analyticsAverageDTOS = analyticsConnector.getAvgStudentRatingsByDisciplineAndGroupIdAndExamId(disciplineId, students, examId);
            }
            analyticsDTOS.addAll(analyticsAverageDTOS);
        }

        List<Rating> ratings = new ArrayList<>();
        for (AbstractAnalyticsDTO analyticsDTO : analyticsDTOS) {
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
            student.setId(analyticsDTO.getStudentId());
            if (groupId != null) {
                Group group = new Group();
                group.setId(groupId);
                student.setPrimaryGroup(group);
            }
            rating.setStudent(student);

            AnalyticGrade analyticGrade = new AnalyticGrade();
            if (analyticsDTO instanceof AnalyticsAverageDTO) {
                analyticGrade.setGrade(((AnalyticsAverageDTO) analyticsDTO).getAvgGrade());
                analyticGrade.setTimeSpent(((AnalyticsAverageDTO) analyticsDTO).getAvgTimeSpent());
            } else {
                analyticGrade.setGrade(((AnalyticsSummaryDTO) analyticsDTO).getSumGrade());
                analyticGrade.setTimeSpent(((AnalyticsSummaryDTO) analyticsDTO).getSumTimeSpent());
            }

            rating.setGrade(analyticGrade);
            rating.setRank(analyticsDTO.getRank());
            ratings.add(rating);
        }
        return ratings;
    }

    public StudentDisciplineStatistic getStudentAnalyticsByLoggedStudent(Long disciplineId) {

        Long studentId = getCurrentUser().getId();

        return getStudentAnalyticsByStudentId(disciplineId, studentId);
    }

    public DisciplineSummary getDisciplineSummaryByGroupForStudent(Long disciplineId) {

        Student student = (Student) getCurrentUser();

        return getDisciplineSummaryByGroup(disciplineId, student.getPrimaryGroup().getId());
    }

    public StudentDisciplineStatistic getStudentAnalyticsByStudentId(Long disciplineId, Long studentId) {

        StudentDisciplineStatistic analyticFull = createStudentDisciplineStatistic(disciplineId, studentId);

        List<StudentTestExecutionSummary> tests = new ArrayList<>();
        List<AnalyticsDTO> analyticsDTOS = analyticsConnector.getByDisciplineIdAndStudentIdAndPersonalExamIdIsNotNull(disciplineId, studentId);

        for (AnalyticsDTO analyticsDTO : analyticsDTOS) {
            StudentTestExecutionSummary testExecutionSummary = new StudentTestExecutionSummary();
            testExecutionSummary.setExam(getExam(analyticsDTO));
            testExecutionSummary.setPersonalExam(getPersonalExam(analyticsDTO));
            tests.add(testExecutionSummary);
        }

        analyticFull.setTests(tests);

        return analyticFull;
    }

    public DisciplineSummary getDisciplineSummary(Long disciplineId) {
        DisciplineSummary disciplineSummary = createDisciplineSummary(disciplineId);

        List<GroupReferenceDTO> groupReferenceDTOS = groupReferenceConnector.findByDisciplineId(disciplineId);
        Set<Long> studentIds = new HashSet<>();
        for (GroupReferenceDTO groupReferenceDTO : groupReferenceDTOS) {
            List<Student> students = userConnector.getStudentOfGroup(groupReferenceDTO.getGroupId());
            studentIds.addAll(students.stream()
                    .map(AbstractUser::getId)
                    .collect(Collectors.toSet())
            );
        }

        disciplineSummary.setStudentsGrade(getStudentsGradeByDiscipline(disciplineId, studentIds));
        disciplineSummary.setTests(getDisciplineSummaryTests(disciplineId, studentIds));

        return disciplineSummary;
    }

    public DisciplineSummary getDisciplineSummaryByGroup(Long disciplineId, Long groupId) {
        DisciplineSummary disciplineSummary = createDisciplineSummary(disciplineId);

        Set<Long> studentIds = new HashSet<>();
        List<Student> students = userConnector.getStudentOfGroup(groupId);
        studentIds.addAll(students.stream()
                .map(AbstractUser::getId)
                .collect(Collectors.toSet())
        );

        disciplineSummary.setStudentsGrade(getStudentsGradeByDiscipline(disciplineId, studentIds));
        disciplineSummary.setTests(getDisciplineSummaryTests(disciplineId, studentIds));

        return disciplineSummary;
    }

    private DisciplineSummary createDisciplineSummary(Long disciplineId) {
        DisciplineSummary disciplineSummary = new DisciplineSummary();
        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        disciplineSummary.setDiscipline(discipline);
        return disciplineSummary;
    }

    private SummaryGrade getStudentsGradeByDiscipline(Long disciplineId, Set<Long> studentIds) {
        SummaryGradeDTO summaryGradeDTO = analyticsConnector.getStudentsGradeByDiscipline(disciplineId, studentIds);
        return summaryGradeConvertor.fromDto(summaryGradeDTO);
    }

    private List<ExamSummary> getDisciplineSummaryTests(Long disciplineId, Set<Long> studentIds) {
        List<SummaryGradeByExamDTO> studentsGradeByExam = analyticsConnector.getStudentsGradeByExam(disciplineId, studentIds);

        return studentsGradeByExam.stream()
                .map(summaryGradeByExamConvertor::fromDto)
                .toList();
    }

    private ExamSummaryStatistic getExam(AnalyticsDTO analyticsDTO) {
        return analyticsConnector.getExamInfo(analyticsDTO.getExamId());
    }

    private PersonalExamSummaryStatistic getPersonalExam(AnalyticsDTO analyticsDTO) {
        PersonalExamSummaryStatistic personalExam = new PersonalExamSummaryStatistic();
        personalExam.setPersonalExamId(analyticsDTO.getPersonalExamId());
        personalExam.setSpentTime(analyticsDTO.getTimeSpent());
        personalExam.setGrade(analyticsDTO.getGrade());
        personalExam.setStartDate(analyticsDTO.getStartDate());
        return personalExam;
    }

    private StudentDisciplineStatistic createStudentDisciplineStatistic(Long disciplineId, Long studentId) {
        StudentDisciplineStatistic studentDisciplineStatistic = new StudentDisciplineStatistic();

        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);

        Student student = new Student();
        student.setId(studentId);

        studentDisciplineStatistic.setDiscipline(discipline);
        studentDisciplineStatistic.setStudent(student);

        SummaryGradeDTO summaryGradeDTO = analyticsConnector.getSummaryGrade(disciplineId, studentId);
        SummaryGrade summaryGrade = summaryGradeConvertor.fromDto(summaryGradeDTO);
        studentDisciplineStatistic.setSummaryGrade(summaryGrade);

        return studentDisciplineStatistic;
    }

    public void removeAnalyticsByDisciplineAndGroup(Long disciplineId,Long groupId){
        List<Long> students = new ArrayList<>();
        if (groupId != null) {
            students = userConnector.getStudentOfGroup(groupId).stream().map(AbstractUser::getId).toList();
        }

        for(Long studentId: students){
            analyticsConnector.deleteAllByDisciplineIdAndStudentId(disciplineId,studentId);
        }
    }
}
