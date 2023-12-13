package com.sytoss.lessons.controllers.views;

import com.sytoss.domain.bom.analytics.SummaryGrade;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ExamGroupSummary extends ExamSummary {

    private Map<Long, SummaryGrade> gradesByGroup = new HashMap<>();
}
