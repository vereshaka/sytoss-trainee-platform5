package com.sytoss.lessons.controllers.views;

import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.lessons.Exam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamSummary {

    protected Exam exam;

    protected SummaryGrade studentsGrade;
}
