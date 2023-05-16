package bom;

import lombok.Getter;
import lombok.Setter;

public enum AnswerStatus {
    STATUS_IN_PROGRESS(0),
    STATUS_NOT_STARTED(1),
    STATUS_NOT_ANSWERED(2),
    STATUS_NOT_GRADED(3);


    @Setter
    @Getter
    private Integer status;

    AnswerStatus(Integer status) {
        this.status = status;
    }
}
