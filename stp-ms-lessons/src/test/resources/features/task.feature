Feature: Task

  Scenario: Retrieve information about task
    Given task with question "What are the different subsets of SQL?" exist
    When retrieve information about this task
    Then operation is successful
    And should return task with "What are the different subsets of SQL?" question

  Scenario: Retrieve information about task when it doesnt exist
    Given task with question "What are the different subsets of SQL?" and id 3 doesnt exist
    When retrieve information about this task with id 3
    Then operation should be finished with 404 "Task with id "3" not found" error