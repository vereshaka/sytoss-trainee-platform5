package com.sytoss.lessons.bdd.common;

import com.sytoss.domain.bom.analytics.Analytics;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.lessons.dto.TaskDomainDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LessonsDetails {

    List<TaskDomainDTO> taskDomains = new ArrayList<>();
    private Long disciplineId;
    private Long topicId;
    private Long teacherId;
    private Long taskId;
    private Long taskDomainId;
    private Long taskConditionId;
    private Long groupReferenceId;
    private List<Long> groupId;
    private List<PersonalExam> personalExams = new ArrayList<>();
    private CheckRequestParameters checkRequestParameters = new CheckRequestParameters();
    private Analytics analytics;
    private List<TaskCondition> taskConditions = new ArrayList<>();
}
