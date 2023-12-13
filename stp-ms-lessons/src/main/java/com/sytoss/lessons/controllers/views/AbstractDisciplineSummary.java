package com.sytoss.lessons.controllers.views;

import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractDisciplineSummary {

    protected Discipline discipline;

    protected SummaryGrade studentsGrade;
}
