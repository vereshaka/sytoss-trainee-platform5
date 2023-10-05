Feature: Exam

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists

  Scenario: teacher create exam
    Given topics exist
      | discipline | topic         |
      | SQL        | Set of Tables |
      | SQL        | Select        |
      | SQL        | Join          |
      | Mongo      | Set of Tables |
    And "SQL" discipline has group with id 8
    When a teacher create "Exam" exam from 22.05.2022 to 24.05.2022 with 5 tasks for this group in "SQL" discipline with 20 minutes duration
      | discipline | topic         |
      | SQL        | Set of Tables |
      | SQL        | Join          |
    Then operation is successful
    And "Exam" exam should be from 22.05.2022 to 24.05.2022 with 5 tasks for this group with 20 minutes duration
    And "Exam" exam for this group should have topics
      | discipline | topic         |
      | SQL        | Set of Tables |
      | SQL        | Join          |

  Scenario: STP-695 Error occurs during exam creation
    When a teacher create exam by request STP-695.json
    Then operation is successful


  Scenario: STP-694 Could not fetch exam list
    Given topics exist
      | discipline | topic  |
      | SQL        | Select |
      | SQL        | Join   |
    And "Trade23" task domain with "task-domain/prod-trade23-db.yml" db and "task-domain/prod-trade23-data.yml" data scripts exists for this discipline
      | question                               | answer             | id   | topics       |
      | What are the different subsets of SQL? | select * from dual | *ta1 | Select, Join |
      | "What is content of dual table?        | select * from dual | *ta2 | Select       |
    And this discipline has exams
      | name  | tasks      | taskCount | maxGrade | id   |
      | Exam1 | *ta1, *ta2 | 2         | 2        | *ex1 |
    When a teacher request exam list
    Then operation is successful