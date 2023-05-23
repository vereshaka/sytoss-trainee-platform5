package com.sytoss.domain.bom.personalexam;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamConfiguration {

    private String disciplineId;

    private List<String> topics;

    private String examName;

    private int quantityOfTask;

    private Long studentId;
}
