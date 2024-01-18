package com.sytoss.lessons.convertors;

import com.sytoss.lessons.controllers.views.ExamAssigneesStatus;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ExamAssigneesStatusConverter {

    public void fromDTO(ExamDTO source, ExamAssigneesStatus destination) {
        destination.setNumberOfTask(source.getNumberOfTasks());
        destination.setTaskCount(source.getTasks().size());

        Date current = new Date();
        destination.setInProgress(source.getExamAssignees().stream().anyMatch(assignee ->
                assignee.getRelevantFrom().compareTo(current) <= 0 && current.compareTo(assignee.getRelevantTo()) < 0));
        destination.setNotStarted(source.getExamAssignees().stream().allMatch(assignee ->
                assignee.getRelevantFrom().after(new Date())));
    }
}