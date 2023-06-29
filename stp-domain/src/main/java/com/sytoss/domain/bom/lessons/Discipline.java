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

    private String shortDescription;

    private String fullDescription;

    private Double duration;

    private byte[] icon;
}
