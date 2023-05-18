package com.sytoss.domain.bom.personalexam;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamConfiguration {

    private Long disciplineId;

    private List<Long> topics;

    private int quantityOfTask;

    private Long studentId;
}
