Feature: Task

  Background:
    Given teacher "Maksym" "Mitkov" exists
    And this teacher has "SQL" discipline
    And this discipline has "Join" topic
    And "First Domain" task domain exists
    And task with question "What are the different subsets of SQL?" exists

  Scenario: add new condition to task
    Given "Select" condition with CONTAINS type does not exist in this task
    When system add "Select" condition with "contains" type to task with question "What are the different subsets of SQL?"
    Then operation is successful
    And "Select" condition with CONTAINS type should be in task with question "What are the different subsets of SQL?"