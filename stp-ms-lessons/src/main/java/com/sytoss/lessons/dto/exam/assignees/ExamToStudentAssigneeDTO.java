package com.sytoss.lessons.dto.exam.assignees;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("S")
@Entity
public class ExamToStudentAssigneeDTO extends ExamAssigneeToDTO {

    private Long studentId;
}
