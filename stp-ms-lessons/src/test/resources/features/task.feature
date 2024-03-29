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

  Scenario: Retrieve information about tasks by topic id
    Given this teacher has "SQL" discipline with id *1 and following topics:
      | topicId | topicName |
      | *2      | Join      |
      | *3      | INSERT    |
    And  this teacher has "Mongo" discipline with id *4 and following topics:
      | topicId | topicName |
      | *5      | Join      |
    And topic with id *2 contains the following tasks:
      | taskName            | code |
      | What is Join?       | 2    |
      | What is Inner Join? | 1    |
    And topic with id *3 contains the following tasks:
      | taskName      | code |
      | What is Join? | 2    |
    And topic with id *5 contains the following tasks:
      | taskName      | code |
      | What is Join? | 2    |
    When retrieve information about tasks of topic with id *2
    Then operation is successful
    And tasks of topic with id *2 should be received
      | discipline | topic | task                | code |
      | SQL        | Join  | What is Inner Join? | 1    |
      | SQL        | Join  | What is Join?       | 2    |

  Scenario: Link task to topic
    Given task with question "What is Join?" exists
    And topic "Inner Join" exists
    When assign topic "Inner Join" to this task
    Then operation is successful
    And task with question "What is Join?" should be assign to "Inner Join" topic

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

  Scenario: Check current correct student's answer
    Given "First Domain" task domain exists
    When request is "select * from Discipline" sent to check this request for this task domain
    Then operation is successful
    And query result should be
      | id | name  |
      | 1  | SQL   |
      | 2  | Mongo |

  Scenario: Delete task
    Given task with question "What are the different subsets of SQL?" exists
    When delete task
    Then operation is successful
    And should return task with "What are the different subsets of SQL?" question

  Scenario: Update information about task
    Given task with question "What are the different subsets of SQL2?" exists
    And task with question "What are the different subsets of SQL2?" has condition "Join" with type CONTAINS
    When teacher updates this task with question "What are the different subsets of SQL2?" question to "What are the different subsets of Mongo?"
    Then operation is successful
    And should return task with "What are the different subsets of Mongo?" question

  Scenario: STP-942 system create new task with task conditions
    Given task with question "What are the different subsets of SQL?" doesnt exist
    When system create task with a question "What are the different subsets of SQL?" and required command "!NULL, DISTINCT, JOIN, !IN, !EXISTS"
    Then operation is successful
    And task with question "What are the different subsets of SQL?" should be created
    And task conditions should be
      | value    | type         |
      | NULL     | NOT_CONTAINS |
      | DISTINCT | CONTAINS     |
      | JOIN     | CONTAINS     |
      | IN       | NOT_CONTAINS |
      | EXISTS   | NOT_CONTAINS |
    And required command should be "!NULL,DISTINCT,JOIN,!IN,!EXISTS"

  Scenario: STP-1044 system create new task with duplicate code in one domain
    Given task with question "What are the different subsets of SQL?" exists
    And this task has code "1"
    When system create task for "First Domain" task domain with code "1" and with question "What are the different subsets of SQL?"
    Then operation should be finished with 409 "Task with code "1" already exists" error

  Scenario: STP-1044 system create new task with duplicate code in different domains
    Given task with question "What are the different subsets of SQL?" exists
    And this task has code "1"
    And "Second Domain" task domain exists
    When system create task for "Second Domain" task domain with code "1" and with question "What are the different subsets of SQL?"
    Then operation is successful
    And tasks with code exist
      | taskDomain    | code |
      | First Domain  | 1    |
      | Second Domain | 1    |