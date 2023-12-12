package com.sytoss.lessons.controllers.viewModel;

import com.sytoss.domain.bom.analytics.SummaryGrade;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ExamGroupSummary extends AbstractExamSummary {

    private Map<Long, SummaryGrade> studentsGrade = new HashMap<>();
}
