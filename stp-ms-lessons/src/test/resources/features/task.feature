Feature: Task

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists
    And "SQL" discipline exists
    And this discipline has "Join" topic
    And "First Domain" task domain exists

  Scenario: Retrieve information about task
    Given task with question "What are the different subsets of SQL?" exists
    When retrieve information about this task
    Then operation is successful
    And should return task with "What are the different subsets of SQL?" question

  Scenario: Retrieve information about task when it doesnt exist
    Given task with id *6 doesnt exist
    When retrieve information about task with id *6
    Then operation should be finished with 404 "Task with id "12345" not found" error

  Scenario: system create new task
    Given task with question "What are the different subsets of SQL?" doesnt exist
    When system create task with question "What are the different subsets of SQL?"
    Then operation is successful
    And task with question "What are the different subsets of SQL?" should be created

  Scenario: system does not create new task when task exists
    Given task with question "What are the different subsets of SQL?" exists
    When system create task with question "What are the different subsets of SQL?"
    Then operation should be finished with 409 "Task with question "What are the different subsets of SQL?" already exist" error

  @Bug
  Scenario: Retrieve information about tasks by topic id
    Given this teacher has "SQL" discipline with id *1 and following topics:
      | topicId | topicName |
      | *2      | Join      |
      | *3      | INSERT    |
    And  this teacher has "Mongo" discipline with id *4 and following topics:
      | topicId | topicName |
      | *5      | Join      |
    And topic with id *2 contains the following tasks:
      | taskName            |
      | What is Join?       |
      | What is Inner Join? |
    And topic with id *3 contains the following tasks:
      | taskName      |
      | What is Join? |
    And topic with id *5 contains the following tasks:
      | taskName      |
      | What is Join? |
   # Given tasks exist
   #   | discipline | topic  | task                |
   #   | SQL        | Join   | What is Join?       |
   #   | SQL        | Join   | What is Inner Join? |
   #   | SQL        | INSERT | What is Join?       |
   #   | Mongo      | Join   | What is Join?       |
    When retrieve information about tasks of topic with id *2
    Then operation is successful
    And tasks should be received
      | discipline | topic | task                |
      | SQL        | Join  | What is Join?       |
      | SQL        | Join  | What is Inner Join? |

  @Bug
  Scenario: Link task to topic
    Given task with question "What is Join?" exists
    And topic "Join" exists
    When assign topic "Join" to this task
    Then operation is successful
    And task with question "What is Join?" should be assign to "Join" topic

  Scenario: Remove one of conditions from the task
    Given tasks exist
      | task          | condition | type     |
      | What is Join? | not null  | CONTAINS |
      | What is Join? | equal     | CONTAINS |
      | What is Join? | not equal | CONTAINS |
    When remove condition "not null" and "CONTAINS" type from task
    Then operation is successful
    And task should be received
      | discipline | topic | task          | condition | type     |
      | SQL        | Join  | What is Join? | equal     | CONTAINS |
      | SQL        | Join  | What is Join? | not equal | CONTAINS |

  Scenario: add new condition to task
    Given task with question "What are the different subsets of SQL?" exists
    And "Select" condition with CONTAINS type does not exist in this task
    When system add "Select" condition with CONTAINS type to task with question "What are the different subsets of SQL?"
    Then operation is successful
    And "Select" condition with CONTAINS type should be in task with question "What are the different subsets of SQL?"