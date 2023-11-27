package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.analytics.AnalyticGrade;
import com.sytoss.domain.bom.analytics.SummaryGrade;
import com.sytoss.lessons.dto.SummaryGradeDTO;
import org.springframework.stereotype.Component;

@Component
public class SummaryGradeConvertor {

    public SummaryGrade fromDto(SummaryGradeDTO summaryGradeDTO){
        AnalyticGrade averageGrade = new AnalyticGrade();
        averageGrade.setGrade(summaryGradeDTO.getAvgGrade());
        averageGrade.setTimeSpent(summaryGradeDTO.getAvgTimeSpent());
        AnalyticGrade maxGrade = new AnalyticGrade();
        maxGrade.setGrade(summaryGradeDTO.getMaxGrade());
        maxGrade.setTimeSpent(summaryGradeDTO.getMaxTimeSpent());

        SummaryGrade summaryGrade = new SummaryGrade();
        summaryGrade.setAverage(averageGrade);
        summaryGrade.setMax(maxGrade);

        return summaryGrade;
    }
}
