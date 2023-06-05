Feature: Task

  Background:
    Given "alskdfj" task domain exist
    And teacher with "Ivan" firstName and "Ivanovich" middleName and "Ivanov" lastName exists
    And "db" discipline exists
    And this discipline has "SQL" topic
    And "TaskDomain" task domain exist

  Scenario: Retrieve information about tasks by topic id
    Given topics exist
      | discipline | topic  | task                |
      | SQL        | Join   | What is Join?       |
      | SQL        | Join   | What is Inner Join? |
      | SQL        | INSERT | What is Join?       |
      | Mongo      | Join   | What is Join?       |
    When retrieve information about tasks by topic
    Then operation is successful
    And tasks should be received
      | discipline | topic  | task                |
      | SQL        | Join   | What is Join?       |
      | SQL        | Join   | What is Inner Join? |

  Scenario: Retrieve information about tasks by topic id when it doesnt exist
    Given topic with name "SQL" and id "1" exist
    And task with question "What are the different subsets of SQL?" doesnt have this topic
    When retrieve information about tasks by topic
    Then operation should be finished with 404 "Task with id "1" not found" error