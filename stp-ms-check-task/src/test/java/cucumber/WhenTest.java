package cucumber;

import com.sytoss.stp.service.GradeService;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class WhenTest extends AbstractCucumberTest {

    @Mock
    private final GradeService gradeService;
    @When("user checks student`s answer with checkResults button using {String},{String}, and {String}")
    public void userChecksAnswerWithEtalonAndDatabaseScript(String answer, String etalon, String databaseScript) throws Exception {
        verify(gradeService,times(1)).checkAndGrade(answer,etalon,databaseScript);
    }
}
