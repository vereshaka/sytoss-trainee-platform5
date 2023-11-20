package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.AnalyticsElement;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.AnalyticsElementDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.when;

@Transactional
public class AnalyticsElementGiven extends AbstractGiven {

    @Given("analytics elements exist")
    public void analyticsElementsExists(DataTable dataTable) throws ParseException {
        List<Map<String, String>> analyticsList = dataTable.asMaps();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
        List<Student> students = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        for (Map<String, String> analytics : analyticsList) {
            String disciplineKey = analytics.get("disciplineId").trim();
            Long groupId = Long.valueOf(analytics.get("groupId"));
            Group group = new Group();
            group.setId(groupId);
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
                if (groupReferenceDTOS.stream().filter(groupReferenceDTO -> Objects.equals(groupReferenceDTO.getGroupId(),groupId)).toList().size() == 0) {
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

            Long studentId = Long.valueOf(analytics.get("studentId"));
            Student student = new Student();
            student.setId(studentId);
            student.setPrimaryGroup(group);

            if(students.stream().filter(el-> Objects.equals(el.getId(), studentId)).toList().size()==0){
                students.add(student);
            }
            if(groups.stream().filter(el-> Objects.equals(el.getId(), groupId)).toList().size()==0){
                groups.add(group);
            }


            if (examAssigneeKey != null) {
                String newExamAssigneeKey = examAssigneeKey;
                if (getTestExecutionContext().replaceId(examAssigneeKey) != null) {
                    newExamAssigneeKey = getTestExecutionContext().replaceId(examAssigneeKey).toString();
                }

                ExamAssigneeDTO examAssigneeDTO;
                if (Objects.equals(examAssigneeKey, newExamAssigneeKey)) {
                    examAssigneeDTO = new ExamAssigneeDTO();
                    examAssigneeDTO.setExam(examDTO);
                    ExamToStudentAssigneeDTO examToStudentAssigneeDTO = new ExamToStudentAssigneeDTO();
                    examToStudentAssigneeDTO.setStudentId(studentId);
                    examToStudentAssigneeDTO.setParent(examAssigneeDTO);
                    examAssigneeDTO = getExamAssigneeConnector().save(examAssigneeDTO);
                    getExamAssigneeToConnector().save(examToStudentAssigneeDTO);
                    examAssigneeDTO.setExamAssigneeToDTOList(List.of(examToStudentAssigneeDTO));
                    getTestExecutionContext().registerId(examAssigneeKey, examAssigneeDTO.getId());
                }
            }

            AnalyticsElementDTO analyticsElementDTO = getAnalyticsConnector().getByDisciplineIdAndExamIdAndStudentId(disciplineId, examId, studentId);
            if (analyticsElementDTO != null) {
                getAnalyticsConnector().deleteAnalyticsElementDTOByDisciplineIdAndStudentId(disciplineId, studentId);
            }

            analyticsElementDTO = new AnalyticsElementDTO();
            analyticsElementDTO.setDisciplineId(disciplineId);
            analyticsElementDTO.setExamId(examId);
            analyticsElementDTO.setStudentId(studentId);
            analyticsElementDTO.setPersonalExamId(personalExamId);
            String grade = analytics.get("grade");
            if (grade != null) {
                analyticsElementDTO.setGrade(Double.parseDouble(grade));
            }
            String timeSpent = analytics.get("timeSpent");
            if (timeSpent != null) {
                analyticsElementDTO.setTimeSpent(Long.parseLong(timeSpent));
            }
            String startDate = analytics.get("startDate");
            if (startDate != null) {
                analyticsElementDTO.setStartDate(sdf.parse(startDate));
            }
            getAnalyticsConnector().save(analyticsElementDTO);
        }

        for(Group group : groups){
            when(getUserConnector().getStudentOfGroup(group.getId())).thenReturn(students.stream().filter(student -> Objects.equals(student.getPrimaryGroup().getId(), group.getId())).toList());
        }
    }

    @Given("^teacher changes grade to$")
    public void teacherChangeGradeTo(DataTable dataTable) {
        Map<String, String> analyticsMap = dataTable.asMaps().get(0);
        AnalyticsElement analyticsElement = new AnalyticsElement();

        Long disciplineId = (Long) getTestExecutionContext().replaceId(analyticsMap.get("disciplineId"));
        Long examId = (Long) getTestExecutionContext().replaceId(analyticsMap.get("examId"));
        Long studentId = Long.parseLong(analyticsMap.get("studentId"));
        String personalExamId = analyticsMap.get("personalExamId").trim().replace("*", "");

        analyticsElement.setDisciplineId(disciplineId);
        analyticsElement.setExamId(examId);
        analyticsElement.setStudentId(studentId);
        analyticsElement.setPersonalExamId(personalExamId);

        if (analyticsMap.get("examAssigneeId") != null) {
            Long examAssigneeId = (Long) getTestExecutionContext().replaceId(analyticsMap.get("examAssigneeId"));
            analyticsElement.setExamAssigneeId(examAssigneeId);
        }
        if (analyticsMap.get("grade") != null) {
            Double grade = Double.parseDouble(analyticsMap.get("grade"));
            analyticsElement.setGrade(grade);
        }
        if (analyticsMap.get("timeSpent") != null) {
            Long timeSpent = Long.parseLong(analyticsMap.get("timeSpent"));
            analyticsElement.setTimeSpent(timeSpent);
        }

        getTestExecutionContext().getDetails().setAnalyticsElement(analyticsElement);
    }
}
