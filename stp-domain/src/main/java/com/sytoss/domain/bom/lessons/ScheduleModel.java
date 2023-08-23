package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ScheduleModel {

    private Date relevantFrom;

    private Date relevantTo;
}
