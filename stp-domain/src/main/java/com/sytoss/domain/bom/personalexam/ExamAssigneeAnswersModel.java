package com.sytoss.domain.bom.personalexam;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamAssigneeAnswersModel {

    private Long examAssigneeId;

    private List<ReviewGradeModel> grades;
}
