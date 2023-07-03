package com.sytoss.domain.bom.personalexam;

import com.sytoss.domain.bom.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamConfiguration {

    private Long disciplineId;


    private List<Long> topics;

    private String examName;

    private Integer time;

    private int quantityOfTask;

    private Student studentId;
}
