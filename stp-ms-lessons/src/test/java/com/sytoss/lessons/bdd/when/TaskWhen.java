package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;

public class TaskWhen extends CucumberIntegrationTest {

    @When("retrieve information about this task")
    public void requestSentRetrieveTask() {
        String url = "/api/task/" + TestExecutionContext.getTestContext().getTaskId();
        ResponseEntity<Task> responseEntity = doGet(url, Void.class, Task.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("retrieve information about this task with id {long}")
    public void requestSentRetrieveTask(long taskId) {
//        TaskDTO taskDTO = getTaskConnector().getByName(disciplineName);
        String url = "/api/task/" + taskId;
        ResponseEntity<String> responseEntity = doGet(url, Void.class, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

}
