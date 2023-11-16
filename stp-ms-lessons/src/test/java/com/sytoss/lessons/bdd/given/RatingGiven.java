package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.Rating;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.RatingDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional
public class RatingGiven extends AbstractGiven {

    @Given("rating exists")
    public void ratingExists(DataTable dataTable) {
        Group group = new Group();
        group.setId(1L);
        getTestExecutionContext().getDetails().setGroupId(List.of(group.getId()));
        List<Map<String, String>> ratingList = dataTable.asMaps();
        for (Map<String, String> rating : ratingList) {
            String disciplineKey = rating.get("disciplineId").trim();
            String examKey = rating.get("examId").trim();
            String examAssigneeKey = rating.get("examAssigneeId");
            if (examAssigneeKey != null) {
                examAssigneeKey = examAssigneeKey.trim();
            }
            String personalExamId = rating.get("personalExamId").trim().replace("*", "");
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
                if (groupReferenceDTOS.stream().filter(groupReferenceDTO -> Objects.equals(groupReferenceDTO.getGroupId(), group.getId())).toList().size() == 0) {
                    GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO();
                    groupReferenceDTO.setGroupId(group.getId());
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

            Long studentId = Long.valueOf(rating.get("studentId"));

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

            RatingDTO ratingDTO = getRatingConnector().getByDisciplineIdAndExamIdAndStudentId(disciplineId, examId, studentId);
            if (ratingDTO != null) {
                getRatingConnector().deleteRatingDTOByDisciplineIdAndStudentId(disciplineId, studentId);
            }

            ratingDTO = new RatingDTO();
            ratingDTO.setDisciplineId(disciplineId);
            ratingDTO.setExamId(examId);
            ratingDTO.setStudentId(studentId);
            ratingDTO.setPersonalExamId(personalExamId);
            String grade = rating.get("grade");
            if (grade != null) {
                ratingDTO.setGrade(Double.parseDouble(grade));
            }
            String timeSpent = rating.get("timeSpent");
            if (timeSpent != null) {
                ratingDTO.setTimeSpent(Long.parseLong(timeSpent));
            }
            getRatingConnector().save(ratingDTO);
        }
    }

    @Given("^teacher changes grade to$")
    public void teacherChangeGradeTo(DataTable dataTable) {
        Map<String, String> ratingMap = dataTable.asMaps().get(0);
        Rating rating = new Rating();

        Long disciplineId = (Long) getTestExecutionContext().replaceId(ratingMap.get("disciplineId"));
        Long examId = (Long) getTestExecutionContext().replaceId(ratingMap.get("examId"));
        Long studentId = Long.parseLong(ratingMap.get("studentId"));
        String personalExamId = ratingMap.get("personalExamId").trim().replace("*", "");

        rating.setDisciplineId(disciplineId);
        rating.setExamId(examId);
        rating.setStudentId(studentId);
        rating.setPersonalExamId(personalExamId);

        if (ratingMap.get("examAssigneeId") != null) {
            Long examAssigneeId = (Long) getTestExecutionContext().replaceId(ratingMap.get("examAssigneeId"));
            rating.setExamAssigneeId(examAssigneeId);
        }
        if (ratingMap.get("grade") != null) {
            Double grade = Double.parseDouble(ratingMap.get("grade"));
            rating.setGrade(grade);
        }
        if (ratingMap.get("timeSpent") != null) {
            Long timeSpent = Long.parseLong(ratingMap.get("timeSpent"));
            rating.setTimeSpent(timeSpent);
        }

        getTestExecutionContext().getDetails().setRating(rating);
    }
}
