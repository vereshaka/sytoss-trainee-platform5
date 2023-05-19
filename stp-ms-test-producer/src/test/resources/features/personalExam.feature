Feature: PersonalExam

  Scenario: system create personal exam
    Given tasks exist
      | discipline | topic           | task       |
      | SQL        | Join            | Inner Join |
      | SQL        | Join            | Left Join  |
      | SQL        | Sorting results | SELECT     |
      | Java       | Sorting results | SELECT     |
    When system create "First" personal exam by "SQL" discipline and "Join" topic with 2 tasks for student with 1L id
    Then operation is successful
    And "First" exam by "SQL" discipline and "Join" topic for student with 1L id should have tasks
      | task       |
      | Inner Join |
      | Left Join  |

  Scenario: Student can start test
    Given personal "Exam" exam for student with 2 id and NOT_STARTED status exist
      | task                                   | task status |
      | What are the different subsets of SQL? | NOT_STARTED |
    When student with 2 id start personal exam "Exam"
    Then operation is successful
    And should return "What are the different subsets of SQL?" question
    And status of "Exam" exam for student with 2 id should be "IN_PROGRESS"
    And status of first answer of "Exam" exam for student with 2 id should be "IN_PROGRESS"

  Scenario: Student is not allowed to start test when it already start
    Given personal "Exam" exam for student with 2 id and IN_PROGRESS status exist
      | task                                   | task status |
      | What are the different subsets of SQL? | IN_PROGRESS |
    When student with 2 id start personal exam "Exam"
    Then operation should be finished with 409 "Exam is already in progress!" error