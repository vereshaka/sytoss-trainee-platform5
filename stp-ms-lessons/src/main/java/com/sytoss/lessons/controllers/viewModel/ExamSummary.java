package com.sytoss.lessons.controllers.viewModel;

import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.lessons.Exam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamSummary {

    private Exam exam;

    private SummaryGrade studentsGrade;

}
