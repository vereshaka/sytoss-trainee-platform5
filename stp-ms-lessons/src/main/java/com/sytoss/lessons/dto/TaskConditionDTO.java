package com.sytoss.lessons.dto;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TASK_CONDITION")
public class TaskConditionDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_condition_id_generator")
    @SequenceGenerator(name = "task_condition_id_generator", sequenceName = "TASK_CONDITION_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ConditionType type;

/*    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TASK_ID", referencedColumnName = "ID")
    private TaskDTO task;*/
}