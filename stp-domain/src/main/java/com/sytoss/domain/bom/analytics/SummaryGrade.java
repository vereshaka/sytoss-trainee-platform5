package com.sytoss.domain.bom.analytics;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryGrade {

    @JsonView({AnalyticFull.AnalyticFullView.class})
    private AnalyticGrade average;

    @JsonView({AnalyticFull.AnalyticFullView.class})
    private AnalyticGrade max;

}
