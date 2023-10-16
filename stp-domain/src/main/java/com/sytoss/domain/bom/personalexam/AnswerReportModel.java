package com.sytoss.domain.bom.personalexam;

import com.sytoss.domain.bom.lessons.TaskReportModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerReportModel {

    private TaskReportModel task;

    private Grade systemGrade;

    private Grade teacherGrade;

    private AnswerStatus answerStatus;

    private Long timeSpent;
}
