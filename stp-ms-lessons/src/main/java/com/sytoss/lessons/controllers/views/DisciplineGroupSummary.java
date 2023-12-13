package com.sytoss.lessons.controllers.views;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DisciplineGroupSummary extends AbstractDisciplineSummary{

    private List<ExamGroupSummary> tests = new ArrayList<>();
}
