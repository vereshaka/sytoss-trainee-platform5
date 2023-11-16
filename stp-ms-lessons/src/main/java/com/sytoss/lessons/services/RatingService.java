package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Rating;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.RatingConnector;
import com.sytoss.lessons.convertors.RatingConvertor;
import com.sytoss.lessons.dto.RatingDTO;
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
public class RatingService extends AbstractService {

    private final RatingConnector ratingConnector;

    private final RatingConvertor ratingConvertor;

    private final ExamConnector examConnector;

    private final UserService userService;

    private final PersonalExamConnector personalExamConnector;

    public List<RatingDTO> initializeRatingsDTOs(Long examId, Long disciplineId, List<Student> students) {
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        for (Long studentId : students.stream().map(AbstractUser::getId).toList()) {
            RatingDTO ratingDTO = new RatingDTO();
            ratingDTO.setDisciplineId(disciplineId);
            ratingDTO.setExamId(examId);
            ratingDTO.setStudentId(studentId);
            ratingDTO.setGrade(0.0);
            ratingDTO.setTimeSpent(0L);
            ratingDTO = ratingConnector.save(ratingDTO);
            ratingDTOS.add(ratingDTO);
        }
        return ratingDTOS;
    }

    public Rating updateRating(Rating rating) {
        if (rating.getExamId() == null && rating.getExamAssigneeId() != null) {
            ExamDTO exam = examConnector.findByExamAssignees_Id(rating.getExamAssigneeId());
            if (exam != null) {
                rating.setExamId(exam.getId());
            }
        }
        RatingDTO ratingDTO = ratingConnector.getByDisciplineIdAndExamIdAndStudentId(rating.getDisciplineId(), rating.getExamId(), rating.getStudentId());
        if (ratingDTO == null) {
            ratingDTO = new RatingDTO();
            ratingConvertor.toDTO(rating, ratingDTO);
        } else {
            if (ratingDTO.getGrade() == null && ratingDTO.getTimeSpent() == null && ratingDTO.getPersonalExamId() == null) {
                ratingDTO.setPersonalExamId(rating.getPersonalExamId());
                ratingDTO.setGrade(rating.getGrade());
                ratingDTO.setTimeSpent(rating.getTimeSpent());
            } else if ((rating.getGrade() >= ratingDTO.getGrade() || ratingDTO.getGrade() == 0) && ratingDTO.getTimeSpent() >= rating.getTimeSpent() || ratingDTO.getTimeSpent() == 0) {
                ratingDTO.setGrade(rating.getGrade());
                ratingDTO.setTimeSpent(rating.getTimeSpent());
                if (ratingDTO.getPersonalExamId() == null || !rating.getPersonalExamId().equals(ratingDTO.getPersonalExamId())) {
                    ratingDTO.setPersonalExamId(rating.getPersonalExamId());
                }
            }
        }

        ratingDTO = ratingConnector.save(ratingDTO);
        ratingConvertor.fromDTO(ratingDTO, rating);
        return rating;
    }

    public List<Rating> migrate(Long disciplineId) {
        List<Student> students = userService.getStudents(disciplineId);
        for (Long studentId : students.stream().map(AbstractUser::getId).toList()) {
            ratingConnector.deleteRatingDTOByDisciplineIdAndStudentId(disciplineId, studentId);
        }

        List<ExamDTO> exams = examConnector.findByTopics_Discipline_Id(disciplineId);
        List<Rating> ratings = new ArrayList<>();
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
            Rating rating = new Rating();
            ExamDTO examDTOByExamAssignee = exams.stream().filter(examDTO -> examDTO.getExamAssignees().stream().map(ExamAssigneeDTO::getId).toList().contains(personalExam.getExamAssigneeId())).toList().get(0);
            rating.setExamId(examDTOByExamAssignee.getId());
            rating.setDisciplineId(disciplineId);
            rating.setStudentId(personalExam.getStudent().getId());
            rating.setPersonalExamId(personalExam.getId());
            rating.setGrade(personalExam.getSummaryGrade());
            rating.setTimeSpent(personalExam.getSpentTime());
            RatingDTO ratingDTO = new RatingDTO();
            ratingConvertor.toDTO(rating, ratingDTO);
            ratingConnector.save(ratingDTO);
            ratings.add(rating);
        }
        return ratings;
    }
}
