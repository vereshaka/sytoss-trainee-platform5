package com.sytoss.lessons.bom;

import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisciplineModel {

    private Long groupId;

    private Discipline discipline;

    private boolean isExcluded;
}
