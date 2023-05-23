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