package com.sytoss.domain.bom.analytics;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticGrade {

    @JsonView({AnalyticFull.AnalyticFullView.class})
    private double grade;

    @JsonView({AnalyticFull.AnalyticFullView.class})
    private long timeSpent;
}
