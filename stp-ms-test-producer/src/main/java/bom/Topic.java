package bom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Topic {

    private Long id;

    private String name;

    private Discipline discipline;
}
