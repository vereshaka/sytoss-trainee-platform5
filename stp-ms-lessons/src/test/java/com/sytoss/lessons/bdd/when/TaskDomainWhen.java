package com.sytoss.lessons.bdd.when;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;

public class TaskDomainWhen extends CucumberIntegrationTest {

    @When("^system create \"(.*)\" task domain$")
    public void requestSentCreateTaskDomain(String name) {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(name);
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId();
        ResponseEntity<TaskDomain> responseEntity = doPost(url, taskDomain, TaskDomain.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system create \"(.*)\" task domain when it exist$")
    public void requestSentCreateTaskDomainWhenItExist(String name) {
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(name);
        String url = "/api/discipline/" + TestExecutionContext.getTestContext().getDisciplineId();
        ResponseEntity<String> responseEntity = doPost(url, taskDomain, String.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^system retrieve information about \"(.*)\" task domain$")
    public void systemTryToFindTaskDomainById(String taskDomainName) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(taskDomainName, TestExecutionContext.getTestContext().getDisciplineId());
        if (taskDomainDTO == null) {
            String url = "/api/taskDomain/123";
            ResponseEntity<String> responseEntity = doGet(url, null, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
        if (taskDomainDTO != null) {
            String url = "/api/taskDomain/" + taskDomainDTO.getId();
            ResponseEntity<TaskDomain> responseEntity = doGet(url, null, TaskDomain.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }

    @When("^teacher updates \"(.*)\" task domain to \"(.*)\"$")
    public void teacherUpdatesTaskDomainTo(String oldNameTaskDomain, String newNameTaskDomain) {
        TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(oldNameTaskDomain, TestExecutionContext.getTestContext().getDisciplineId());
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(newNameTaskDomain);
        if(taskDomainDTO == null){
            String url = "/api/taskDomain/123";
            ResponseEntity<String> responseEntity = doPost(url, taskDomain, String.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
        if(taskDomainDTO != null){
            String url = "/api/taskDomain/" + taskDomainDTO.getId();
            ResponseEntity<TaskDomain> responseEntity = doPost(url, taskDomain, TaskDomain.class);
            TestExecutionContext.getTestContext().setResponse(responseEntity);
        }
    }
}
