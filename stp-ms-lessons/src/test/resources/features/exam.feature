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
    When a teacher create "Exam" exam from 22.05.2022 to 24.05.2022 with 5 tasks for "JAVA-1" group in "SQL" discipline with 20 minutes duration
      | discipline | topic         |
      | SQL        | Set of Tables |
      | SQL        | Join          |
    Then operation is successful
    And "Exam" exam should be from 22.05.2022 to 24.05.2022 with 5 tasks for this group with 20 minutes duration
    And "Exam" exam for this group should have topics
      | discipline | topic         |
      | SQL        | Set of Tables |
      | SQL        | Join          |
