Feature: Task Domain

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists
    And "SQL" discipline exists for this teacher

  Scenario: system create a new task domain
    Given "First Domain" task domain doesnt exist
    When system create "First Domain" task domain
    Then operation is successful
    And "First Domain" task domain should exists

  Scenario: system not create a new task domain when it exist
    Given "First Domain" task domain exists
    When system create "First Domain" task domain when it exist
    Then operation should be finished with 409 "TaskDomain with name "First Domain" already exist" error

  Scenario: system get task domain by id
    Given "First Domain" task domain exists
    When system retrieve information about "First Domain" task domain
    Then operation is successful
    And system should been get "First Domain" information

  Scenario: system get not existing task domain by id
    Given "First Domain" task domain doesnt exist
    When system retrieve information about "First Domain" task domain
    Then operation should be finished with 404 "Task Domain with id "123" not found" error

  Scenario: Retrieve information about task domain by discipline name
    Given task domains exist
      | discipline  | task domain   |
      | SQL         | Join          |
      | POSTGRE_SQL | Join          |
      | SQL         | Select        |
      | Mongo       | Join          |
      | SQL         | Set of Tables |
    When receive all task domains by "SQL" discipline
    Then operation is successful
    And task domains should received
      | discipline | task domain   |
      | SQL        | Join          |
      | SQL        | Select        |
      | SQL        | Set of Tables |

  Scenario: Update task domain
    Given "First Domain" task domain exists
    When teacher updates "First Domain" task domain to "Second Domain"
    Then operation is successful
    And "Second Domain" task domain should exists

  Scenario: Update task domain when task domain does not exist
    Given "First Domain" task domain doesnt exist
    When teacher updates "First Domain" task domain to "Second Domain"
    Then operation should be finished with 404 "Task Domain with id "123" not found" error

  Scenario: system generate scheme image and save in task domain
    Given "First Domain" task domain exists
    And "First Domain" task domain doesn't have image
    When system generate image of scheme and save in "First Domain" task domain
    Then operation is successful
    And  "First Domain" should have image

  Scenario: Update task domain when personal exam does not finished
    Given  "First Domain" task domain with a script from "task-domain/script.yml" exists for this discipline
    And personal exams exist
      | examName         | status       | task domain  |
      | SQL Querry       | Graded       | First Domain |
      | SQL Querry Last  | Not finished | First Domain |
      | SQL Querry Third | Graded       | First Domain |
    When teacher updates "First Domain" task domain to "Second Domain"
    Then operation should be finished with 409 "Task domain with First Domain doesnt update because personal exam not finished" error

  Scenario: Get count of tasks of task domain
    Given "First Domain" task domain exists
    And task with question "What are the different subsets of SQL?" exists for this task domain
    When system retrieve information about "First Domain" task domain tasks count
    Then operation is successful
    And task domain have 1 tasks

  Scenario: Get tasks of task domain
    Given task domains exist
      | discipline  | task domain | id |
      | SQL         | Join domain | *1 |
      | POSTGRE_SQL | Join domain | *2 |
    And task domain tasks exist
      | task                | taskDomainId |
      | What is Join?       | *1           |
      | What is Join?       | *2           |
      | What is Inner Join? | *1           |
    When system retrieve information about *1 task domain tasks
    Then operation is successful
    And task domain have tasks
      | task                | taskDomainId |
      | What is Join?       | *1           |
      | What is Inner Join? | *1           |

  Scenario: system generate empty scheme image and save in task domain
    Given "First Domain" task domain exists
    And "First Domain" task domain doesn't have image
    When system generate image of scheme and save in "First Domain" task domain with empty script
    Then operation is successful
    And  "First Domain" should have image
