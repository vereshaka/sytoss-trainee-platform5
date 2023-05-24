package bom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CheckAnswerRequestBody {

    private String answer;

    private String etalon;

    private String script;
}
