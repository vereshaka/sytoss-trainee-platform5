Feature: Task

  Background:
    Given "TaskDomain" task domain exist
    And teacher with "Ivan" firstName and "Jovanovich" middleName and "Ivanov" lastName exists

  Scenario: Retrieve information about tasks by topic id
    Given tasks exist
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