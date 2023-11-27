package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.lessons.controllers.viewModel.ExamSummary;
import com.sytoss.lessons.dto.SummaryGradeByExamDTO;
import org.springframework.stereotype.Component;

@Component
public class SummaryGradeByExamConvertor {
    public ExamSummary fromDto(SummaryGradeByExamDTO examDto) {
        ExamSummary examSummary = new ExamSummary();

        Exam exam = new Exam();
        exam.setName(examDto.getName());
        exam.setMaxGrade(examDto.getExamMaxGrade());
        exam.setId(examDto.getExamId());

        AnalyticGrade averageGrade = new AnalyticGrade();
        averageGrade.setGrade(examDto.getAvgGrade());
        averageGrade.setTimeSpent(examDto.getAvgTimeSpent());
        AnalyticGrade maxGrade = new AnalyticGrade();
        maxGrade.setGrade(examDto.getMaxGrade());
        maxGrade.setTimeSpent(examDto.getMaxTimeSpent());

        SummaryGrade summaryGrade = new SummaryGrade();
        summaryGrade.setAverage(averageGrade);
        summaryGrade.setMax(maxGrade);

        examSummary.setStudentsGrade(summaryGrade);
        examSummary.setExam(exam);

        return examSummary;
    }
}
