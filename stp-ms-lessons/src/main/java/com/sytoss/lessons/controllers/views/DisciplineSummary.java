package com.sytoss.lessons.controllers.views;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DisciplineSummary extends AbstractDisciplineSummary {

    private List<ExamSummary> tests = new ArrayList<>();
}
