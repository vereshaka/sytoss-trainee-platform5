package com.sytoss.domain.bom.lessons;

import com.sytoss.domain.bom.users.Teacher;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discipline {

    private Long id;

    private String name;

    private Teacher teacher;

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof Discipline)) return false;
        Discipline discipline = (Discipline) object;
        return discipline.getId() == id && (name == discipline.getName() || name != null && name.equals(discipline.getName()))
                && (teacher != null &&teacher.equals(discipline.getTeacher()));
    }
}
