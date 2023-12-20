package com.sytoss.lessons.bom;

import com.sytoss.domain.bom.users.Group;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupAssignment {

    private Group group;

    private boolean isExcluded;
}
