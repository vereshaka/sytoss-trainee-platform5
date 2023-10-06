package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.lessons.examassignee.ExamGroupAssignee;
import com.sytoss.domain.bom.lessons.examassignee.ExamStudentAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeToDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToGroupAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExamAssigneeConvertor {

    public void toDTO(ExamAssignee source, ExamAssigneeDTO destination) {
        destination.setId(source.getId());
        destination.setDuration(source.getDuration());
        destination.setRelevantTo(source.getRelevantTo());
        destination.setRelevantFrom(source.getRelevantFrom());
    }

    public void fromDTO(ExamAssigneeDTO source, ExamAssignee destination) {
        destination.setId(source.getId());
        destination.setDuration(source.getDuration());
        destination.setRelevantTo(source.getRelevantTo());
        destination.setRelevantFrom(source.getRelevantFrom());

        for (ExamAssigneeToDTO examAssigneeToDTO : source.getExamAssigneeToDTOList()) {

            if (examAssigneeToDTO instanceof ExamToGroupAssigneeDTO) {
                Group group = new Group();
                group.setId(((ExamToGroupAssigneeDTO) examAssigneeToDTO).getGroupId());
                ((ExamGroupAssignee) destination).getGroups().add(group);
            } else {
                Student student = new Student();
                student.setId(((ExamToStudentAssigneeDTO) examAssigneeToDTO).getStudentId());
                ((ExamStudentAssignee) destination).getStudents().add(student);
            }
        }
    }
}
