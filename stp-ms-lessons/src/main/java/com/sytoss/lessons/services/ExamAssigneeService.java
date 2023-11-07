package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.ScheduleModel;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.personalexam.ExamConfiguration;
import com.sytoss.lessons.connectors.ExamAssigneeConnector;
import com.sytoss.lessons.connectors.ExamAssigneeToConnector;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.convertors.ExamAssigneeConvertor;
import com.sytoss.lessons.convertors.ExamConvertor;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeToDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToGroupAssigneeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamAssigneeService extends AbstractService {

    private final ExamConnector examConnector;

    private final ExamConvertor examConvertor;

    private final PersonalExamConnector personalExamConnector;

    private final ExamAssigneeConvertor examAssigneeConvertor;

    private final ExamAssigneeConnector examAssigneeConnector;

    private final ExamAssigneeToConnector examAssigneeToConnector;

    public ExamAssignee reschedule(ScheduleModel scheduleModel, Long examAssigneeId) {
        ExamAssigneeDTO examToUpdateAssigneeDTO = examAssigneeConnector.getReferenceById(examAssigneeId);
        ExamAssignee examToUpdateAssignee = new ExamAssignee();
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
        ExamAssignee examAssignee = new ExamAssignee();
        examAssigneeConvertor.fromDTO(examAssigneeDTO, examAssignee);
        return examAssignee;
    }

    public void deleteAllByExamId(Long examId) {
        List <ExamAssigneeDTO> examAssigneeDTOS =  examAssigneeConnector.getAllByExam_Id(examId);
        for (ExamAssigneeDTO examAssignee :
                examAssigneeDTOS) {
            deleteAllExamAssigneeToByExamAssignee(examAssignee.getId());
        }
        examAssigneeConnector.deleteAll(examAssigneeDTOS);
    }

    public List<ExamAssignee> findExamAssigneesByGroup(Long groupId) {
        List<ExamToGroupAssigneeDTO> examToGroupAssigneeDTOList = examAssigneeToConnector.findByGroupIdOrderByParent_RelevantFromDesc(groupId);
        return examToGroupAssigneeDTOList.stream().map(examToGroupAssigneeDTO -> {
            ExamAssignee examAssignee = new ExamAssignee();
            examAssigneeConvertor.fromDTO(examToGroupAssigneeDTO.getParent(), examAssignee);
            return examAssignee;
        }).collect(Collectors.toList());
    }

    private void deleteAllExamAssigneeToByExamAssignee(Long examAssigneeId) {
       examAssigneeToConnector.deleteAllByParent_Id(examAssigneeId);
    }
}
