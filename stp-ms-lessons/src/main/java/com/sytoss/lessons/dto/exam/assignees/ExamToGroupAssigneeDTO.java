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
@DiscriminatorValue("G")
public class ExamToGroupAssigneeDTO extends ExamAssigneeToDTO {

    private Long groupId;
}
