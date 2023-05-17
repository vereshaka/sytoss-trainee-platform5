package com.sytoss.producer.bom;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamConfiguration {

    private Long disciplineId;

    private List<Long> topics;

    private String examName;

    private int quantityOfTask;

    private Long studentId;
}
