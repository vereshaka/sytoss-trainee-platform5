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

  Scenario: system create new task
    Given task with question "What are the different subsets of SQL?" doesnt exist
    When system create task with question "What are the different subsets of SQL?"
    Then operation is successful
    And task with question "What are the different subsets of SQL?" should be created

  Scenario: system does not create new task when task exists
    Given task with question "What are the different subsets of SQL?" exists
    When system create task with question "What are the different subsets of SQL?"
    Then operation should be finished with 409 "Task with question "What are the different subsets of SQL?" already exist" error

  Scenario: Retrieve information about tasks by topic id
    Given tasks exist
      | discipline | topic  | task                |
      | SQL        | Join   | What is Join?       |
      | SQL        | Join   | What is Inner Join? |
      | SQL        | INSERT | What is Join?       |
      | Mongo      | Join   | What is Join?       |
    When retrieve information about tasks by "Join" topic in "SQL" discipline
    Then operation is successful
    And tasks should be received
      | discipline | topic  | task                |
      | SQL        | Join   | What is Join?       |
      | SQL        | Join   | What is Inner Join? |

  Scenario: add new condition to task
    Given task with question "What are the different subsets of SQL?" doesnt exist
    And "Select" condition with CONTAINS type does not exist in this task
    When system add "Select" condition with CONTAINS type to task with question "What are the different subsets of SQL?"
    Then operation is successful
    And "Select" condition with CONTAINS type should be in task with question "What are the different subsets of SQL?"