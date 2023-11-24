package com.sytoss.lessons.controllers.viewModel;

import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DisciplineSummary {

    private Discipline discipline;

    private SummaryGrade studentsGrade;

    private List<ExamSummary> tests;

}
