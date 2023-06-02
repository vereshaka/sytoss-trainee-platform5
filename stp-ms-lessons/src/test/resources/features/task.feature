Feature: Task

  Scenario: Retrieve information about task by topic id
    Given topic with name "SQL" exist
    And this topic has task with question "What are the different subsets of SQL?"
    When retrieve information about task by topic
    Then operation is successful
    And should return task with question "What are the different subsets of SQL?"

  Scenario: Retrieve information about task by topic id when it doesnt exist
    Given topic with name "SQL" and id "1" exist
    And task with question "What are the different subsets of SQL?" doesnt have this topic
    When retrieve information about task by topic
    Then operation should be finished with 404 "Task with id "1" not found" error