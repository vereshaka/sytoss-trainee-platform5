package com.sytoss.domain.bom.personalexam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PersonalExamReportModel {

    private String examName;

    private String groupName;

    private String studentName;

    private Long studentId;

    private Date startDate;

    private String email;

    private Double summary;

    private Long spentTime;

    private PersonalExamStatus personalExamStatus;

    private List<AnswerReportModel> answers;
}
