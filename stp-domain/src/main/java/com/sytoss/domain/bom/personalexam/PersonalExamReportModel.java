package com.sytoss.domain.bom.personalexam;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonalExamReportModel {

    private String examName;

    private String groupName;

    private String studentName;

    private String email;

    private Float summary;

    private PersonalExamStatus personalExamStatus;

    private List<AnswerReportModel> answers;
}
