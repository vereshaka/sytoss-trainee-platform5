package com.sytoss.traineeplatform.requestbodies;

import com.sytoss.traineeplatform.bom.AnswerStatus;
import com.sytoss.traineeplatform.bom.Task;
import lombok.Data;

@Data
public class AnswerRequestBody {

    private String value;

    private AnswerStatus status;
}
