package com.sytoss.domain.bom.personalexam;

import com.sytoss.domain.bom.exceptions.businessException.PersonalExamAlreadyStartedException;
import com.sytoss.domain.bom.exceptions.businessException.PersonalExamIsFinishedException;
import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PersonalExam {

    @MongoId
    private String id;

    private String name;

    private Discipline discipline;

    private Date date;

    private Long studentId;

    private List<Answer> answers;

    private PersonalExamStatus status;

    private float summaryGrade;

    public void start() {
        if (status.equals(PersonalExamStatus.NOT_STARTED)) {
            status = PersonalExamStatus.IN_PROGRESS;
        } else if (status.equals(PersonalExamStatus.IN_PROGRESS)) {
            throw new PersonalExamAlreadyStartedException();
        } else {
            throw new PersonalExamIsFinishedException();
        }
    }
}
