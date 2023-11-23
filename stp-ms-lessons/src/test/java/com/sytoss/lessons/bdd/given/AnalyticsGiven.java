package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.Analytic;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.dto.AnalyticsDTO;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.mockito.Mockito.when;

@Transactional
public class AnalyticsGiven extends AbstractGiven {

    @Given("analytics elements exist")
    public void analyticsElementsExists(DataTable dataTable) throws ParseException {
        List<Map<String, String>> analyticsList = dataTable.asMaps();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
        List<Student> students = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        Long groupId = 1L;
        Long studentId;
        for(Map<String, String> analytics : analyticsList){
            if (analytics.get("groupId") != null) {
                groupId = Long.valueOf(analytics.get("groupId"));
            }
            final Long finalGroupId = groupId;
            Group group = new Group();
            group.setId(groupId);

            studentId = Long.valueOf(analytics.get("studentId"));
            final Long finalStudentId = studentId;
            Student student = new Student();
            student.setId(studentId);
            student.setPrimaryGroup(group);
            if (students.stream().filter(el -> Objects.equals(el.getId(), finalStudentId)).toList().size() == 0) {
                students.add(student);
            }
            if (groups.stream().filter(el -> Objects.equals(el.getId(), finalGroupId)).toList().size() == 0) {
                groups.add(group);
            }
        }

        for (Map<String, String> analytics : analyticsList) {
            String disciplineKey = analytics.get("disciplineId").trim();

            if (analytics.get("groupId") != null) {
                groupId = Long.valueOf(analytics.get("groupId"));
            }
            final Long finalGroupId = groupId;
            String examKey = analytics.get("examId").trim();
            String examAssigneeKey = analytics.get("examAssigneeId");
            if (examAssigneeKey != null) {
                examAssigneeKey = examAssigneeKey.trim();
            }
            String personalExamId = analytics.get("personalExamId").trim().replace("*", "");
            String newDisciplineKey = disciplineKey;
            if (getTestExecutionContext().replaceId(disciplineKey) != null) {
                newDisciplineKey = getTestExecutionContext().replaceId(disciplineKey).toString();
            }
            Long disciplineId;
            DisciplineDTO disciplineDTO;
            if (!disciplineKey.equals(newDisciplineKey)) {
                disciplineId = Long.parseLong(newDisciplineKey);
                disciplineDTO = getDisciplineConnector().findById(disciplineId).orElse(null);
                List<GroupReferenceDTO> groupReferenceDTOS = getGroupReferenceConnector().findByDisciplineId(disciplineId);
                if (groupReferenceDTOS.stream().filter(groupReferenceDTO -> Objects.equals(groupReferenceDTO.getGroupId(), finalGroupId)).toList().size() == 0) {
                    GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO();
                    groupReferenceDTO.setGroupId(groupId);
                    groupReferenceDTO.setDiscipline(disciplineDTO);
                    getGroupReferenceConnector().save(groupReferenceDTO);
                }
            } else {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setName("Test");
                disciplineDTO.setTeacherId(1L);
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
                getTestExecutionContext().registerId(disciplineKey, disciplineDTO.getId());
                disciplineId = (Long) getTestExecutionContext().replaceId(disciplineKey);
            }
            String newExamKey = examKey;
            if (getTestExecutionContext().replaceId(examKey) != null) {
                newExamKey = getTestExecutionContext().replaceId(examKey).toString();
            }
            Long examId;
            ExamDTO examDTO;
            if (!Objects.equals(newExamKey, examKey)) {
                examId = Long.parseLong(newExamKey);
                examDTO = getExamConnector().findById(examId).orElse(null);
            } else {
                examDTO = new ExamDTO();
                examDTO.setName("Exam 1");
                TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId("Topic1", disciplineDTO.getId());
                if (topicDTO == null) {
                    topicDTO = new TopicDTO();
                    topicDTO.setName("Topic1");
                    topicDTO.setDiscipline(disciplineDTO);
                    getTopicConnector().save(topicDTO);
                    examDTO.setTopics(List.of(topicDTO));
                }
                examDTO.setDiscipline(disciplineDTO);
                examDTO.setTeacherId(1L);
                examDTO.setCreationDate(Timestamp.from(Instant.now()));
                examDTO = getExamConnector().save(examDTO);
                getTestExecutionContext().registerId(examKey, examDTO.getId());
                examId = (Long) getTestExecutionContext().replaceId(examKey);
            }

            studentId = Long.valueOf(analytics.get("studentId"));

            if (examAssigneeKey != null) {
                String newExamAssigneeKey = examAssigneeKey;
                if (getTestExecutionContext().replaceId(examAssigneeKey) != null) {
                    newExamAssigneeKey = getTestExecutionContext().replaceId(examAssigneeKey).toString();
                }
                ExamAssigneeDTO examAssigneeDTO;
                if (Objects.equals(examAssigneeKey, newExamAssigneeKey)) {
                    examAssigneeDTO = new ExamAssigneeDTO();
                    examAssigneeDTO.setExam(examDTO);
                    List<ExamAssigneeToDTO> examAssigneeToDTOS = new ArrayList<>();
                    examAssigneeDTO = getExamAssigneeConnector().save(examAssigneeDTO);
                    if(groups.isEmpty()){
                        for(Student student: students){
                            ExamToStudentAssigneeDTO examToStudentAssigneeDTO = new ExamToStudentAssigneeDTO();
                            examToStudentAssigneeDTO.setStudentId(student.getId());
                            examToStudentAssigneeDTO.setParent(examAssigneeDTO);
                            getExamAssigneeToConnector().save(examToStudentAssigneeDTO);
                            examAssigneeToDTOS.add(examToStudentAssigneeDTO);
                        }
                    }
                    else{
                        for(Group group : groups){
                            ExamToGroupAssigneeDTO examToGroupAssigneeDTO = new ExamToGroupAssigneeDTO();
                            examToGroupAssigneeDTO.setGroupId(group.getId());
                            examToGroupAssigneeDTO.setParent(examAssigneeDTO);
                            getExamAssigneeToConnector().save(examToGroupAssigneeDTO);
                            examAssigneeToDTOS.add(examToGroupAssigneeDTO);
                        }
                    }

                    examAssigneeDTO.setExamAssigneeToDTOList(examAssigneeToDTOS);
                    getTestExecutionContext().registerId(examAssigneeKey, examAssigneeDTO.getId());
                }
            }

            AnalyticsDTO analyticsDTO = getAnalyticsConnector().getByDisciplineIdAndExamIdAndStudentId(disciplineId, examId, studentId);
            if (analyticsDTO != null) {
                getAnalyticsConnector().deleteAnalyticsDTOByDisciplineIdAndStudentId(disciplineId, studentId);
            }
            analyticsDTO = new AnalyticsDTO();
            analyticsDTO.setDisciplineId(disciplineId);
            analyticsDTO.setExamId(examId);
            analyticsDTO.setStudentId(studentId);
            analyticsDTO.setPersonalExamId(personalExamId);
            String grade = analytics.get("grade");
            if (grade != null) {
                analyticsDTO.setGrade(Double.parseDouble(grade));
            }
            String timeSpent = analytics.get("timeSpent");
            if (timeSpent != null) {
                analyticsDTO.setTimeSpent(Long.parseLong(timeSpent));
            }
            String startDate = analytics.get("startDate");
            if (startDate != null) {
                analyticsDTO.setStartDate(sdf.parse(startDate));
            }
            getAnalyticsConnector().save(analyticsDTO);
        }
        for (Group group : groups) {
            when(getUserConnector().getStudentOfGroup(group.getId())).thenReturn(students.stream().filter(student -> Objects.equals(student.getPrimaryGroup().getId(), group.getId())).toList());
        }
    }

    @Given("^teacher changes grade to$")
    public void teacherChangeGradeTo(DataTable dataTable) {
        Map<String, String> analyticsMap = dataTable.asMaps().get(0);
        Analytic analytic = new Analytic();
        Long disciplineId = (Long) getTestExecutionContext().replaceId(analyticsMap.get("disciplineId"));
        Long examId = (Long) getTestExecutionContext().replaceId(analyticsMap.get("examId"));
        Long studentId = Long.parseLong(analyticsMap.get("studentId"));
        String personalExamId = analyticsMap.get("personalExamId").trim().replace("*", "");
        Discipline discipline = new Discipline();
        discipline.setId(disciplineId);
        analytic.setDiscipline(discipline);
        Exam exam = new Exam();
        exam.setId(examId);
        analytic.setExam(exam);
        Student student = new Student();
        student.setId(studentId);
        analytic.setStudent(student);
        PersonalExam personalExam = new PersonalExam();
        personalExam.setId(personalExamId);
        analytic.setPersonalExam(personalExam);
        if (analyticsMap.get("examAssigneeId") != null) {
            Long examAssigneeId = (Long) getTestExecutionContext().replaceId(analyticsMap.get("examAssigneeId"));
            ExamAssignee examAssignee = new ExamAssignee();
            examAssignee.setId(examAssigneeId);
            analytic.getExam().getExamAssignees().add(examAssignee);
        }
        AnalyticGrade analyticGrade = new AnalyticGrade();
        if (analyticsMap.get("grade") != null) {
            Double grade = Double.parseDouble(analyticsMap.get("grade"));
            analyticGrade.setGrade(grade);
            analytic.setGrade(analyticGrade);
        }
        if (analyticsMap.get("timeSpent") != null) {
            Long timeSpent = Long.parseLong(analyticsMap.get("timeSpent"));
            analyticGrade.setTimeSpent(timeSpent);
            analytic.setGrade(analyticGrade);
        }
        getTestExecutionContext().getDetails().setAnalytic(analytic);
    }

    @Given("^analytics is empty$")
    public void analyticsIsEmpty() {
        getAnalyticsConnector().deleteAll();
    }
}
