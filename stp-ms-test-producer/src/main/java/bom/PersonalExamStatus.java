package bom;

import lombok.Getter;
import lombok.Setter;

public enum PersonalExamStatus {
    STATUS_IN_PROGRESS(0),
    STATUS_NOT_STARTED(1),
    STATUS_FINISHED(2);

    @Setter
    @Getter
    private Integer status;

    PersonalExamStatus(Integer status) {
        this.status = status;
    }
}
