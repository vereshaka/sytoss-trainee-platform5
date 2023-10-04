package com.sytoss.lessons.dto.exam.assignees;

import jakarta.persistence.DiscriminatorValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("S")
public class ExamToStudentAssigneeDTO extends ExamAssigneeToDTO {

    private Long studentId;
}
