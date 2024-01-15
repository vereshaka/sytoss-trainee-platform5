package com.sytoss.lessons.convertors;

import com.sytoss.lessons.controllers.views.ExamAssigneesStatus;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ExamAssigneesStatusConverter {
    public ExamAssigneesStatus fromDTO(ExamDTO source) {
        ExamAssigneesStatus destination = new ExamAssigneesStatus();
        destination.setNumberOfTask(source.getNumberOfTasks());
        destination.setTaskCount(source.getTasks().size());
        destination.setInProgress(source.getExamAssignees().stream().anyMatch(this::isAssigneeInProgress));
        destination.setNotStarted(source.getExamAssignees().stream().allMatch(this::isAssigneeNotStarted));
        return destination;
    }

    private Boolean isAssigneeInProgress(ExamAssigneeDTO assignee) {
        Date current = new Date();
        return assignee.getRelevantFrom().compareTo(current) <= 0 && current.compareTo(assignee.getRelevantTo()) < 0;
    }

    private Boolean isAssigneeNotStarted(ExamAssigneeDTO assignee) {
        return assignee.getRelevantFrom().after(new Date());
    }
}