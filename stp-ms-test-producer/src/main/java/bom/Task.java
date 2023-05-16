package bom;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Task {

    private Long id;

    private String question;

    private String etalonAnswer;

    private TaskDomain taskDomain;

    private List<Topic> topics;

    private List<Condition> conditions;
}
