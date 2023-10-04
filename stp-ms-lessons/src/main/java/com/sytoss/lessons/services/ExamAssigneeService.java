package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.UserNotIdentifiedException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.ExamNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.ScheduleModel;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.lessons.examassignee.ExamGroupAssignee;
import com.sytoss.domain.bom.lessons.examassignee.ExamStudentAssignee;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.ExamAssigneeConnector;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.UserConnector;
import com.sytoss.lessons.convertors.ExamAssigneeConvertor;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToGroupAssigneeDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamAssigneeService extends AbstractService {

    private final ExamConnector examConnector;

    private final ExamConvertor examConvertor;

    private final PersonalExamConnector personalExamConnector;

    private final ExamAssigneeConvertor examAssigneeConvertor;

    private final ExamAssigneeConnector examAssigneeConnector;

    public ExamAssignee reschedule(ScheduleModel scheduleModel, Long examAssigneeId) {
        ExamAssigneeDTO examToUpdateAssigneeDTO = examAssigneeConnector.getReferenceById(examAssigneeId);
        ExamAssignee examToUpdateAssignee;
        if (examToUpdateAssigneeDTO.getExamAssigneeToDTOList().get(0) instanceof ExamToGroupAssigneeDTO) {
            examToUpdateAssignee = new ExamGroupAssignee();
        } else {
            examToUpdateAssignee = new ExamStudentAssignee();
        }
        examAssigneeConvertor.fromDTO(examToUpdateAssigneeDTO, examToUpdateAssignee);
        examToUpdateAssignee.setRelevantTo(scheduleModel.getRelevantTo());
        examToUpdateAssignee.setRelevantFrom(scheduleModel.getRelevantFrom());
        examAssigneeConvertor.toDTO(examToUpdateAssignee, examToUpdateAssigneeDTO);

        examToUpdateAssigneeDTO = examAssigneeConnector.save(examToUpdateAssigneeDTO);
        examAssigneeConvertor.fromDTO(examToUpdateAssigneeDTO, examToUpdateAssignee);

        ExamConfiguration examConfiguration = new ExamConfiguration();
        examConfiguration.setExamAssignee(examToUpdateAssignee);
        personalExamConnector.reschedule(examConfiguration);

        return examToUpdateAssignee;
    }

    public List<ExamAssignee> returnExamAssignees(Long examId) {
        ExamDTO examDTO = examConnector.getReferenceById(examId);
        Exam exam = new Exam();
        examConvertor.fromDTO(examDTO, exam);
        return exam.getExamAssignees();
    }

    public ExamAssignee returnExamAssigneeById(Long examAssigneeId) {
        ExamAssigneeDTO examAssigneeDTO = examAssigneeConnector.getReferenceById(examAssigneeId);
        ExamAssignee examAssignee;
        if (examAssigneeDTO.getExamAssigneeToDTOList().get(0) instanceof ExamToGroupAssigneeDTO) {
            examAssignee = new ExamGroupAssignee();
        } else {
            examAssignee = new ExamStudentAssignee();
        }
        //todo IvanL find another way to get examAssignee without constant comparison. Implement comparison into the ExamAssignee class?

        examAssigneeConvertor.fromDTO(examAssigneeDTO, examAssignee);
        return examAssignee;
    }
}
