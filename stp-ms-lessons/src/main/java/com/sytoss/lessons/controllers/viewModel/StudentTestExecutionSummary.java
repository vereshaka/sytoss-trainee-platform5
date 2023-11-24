package com.sytoss.lessons.controllers.viewModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentTestExecutionSummary {

    private ExamSummaryStatistic exam;

    private PersonalExamSummaryStatistic personalExam;

}
