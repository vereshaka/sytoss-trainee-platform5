Feature: Task

  Background:
    Given teacher "Maksym" "Mitkov" exists
    And this teacher has "SQL" discipline
    And this discipline has "Join" topic
    And "First Domain" task domain exists

  Scenario: Retrieve information about task
    Given task with question "What are the different subsets of SQL?" exists
    When retrieve information about this task
    Then operation is successful
    And should return task with "What are the different subsets of SQL?" question

  Scenario: Retrieve information about task when it doesnt exist
    Given task with question "What are the different subsets of SQL?" doesnt exist
    When retrieve information about this existing task
    Then operation should be finished with 404 "Task with id "1" not found" error