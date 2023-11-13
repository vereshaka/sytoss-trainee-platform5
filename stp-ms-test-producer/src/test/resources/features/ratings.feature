Feature: Answer

  Background:
    Given teacher "John" "Teacher" with "sytoss@gmail.com" email exists

  Scenario: gets average estimate per group
    Given personal exams are exist
      | examName | studentId | groupId | grade | status      |
      | SQL      | 1         | 1       | 10    | FINISHED    |
      | SQL      | 2         | 1       | 13.00 | REVIEWED    |
      | Mongo    | 1         | 2       | 10    | FINISHED    |
      | SQL      | 3         | 1       | 10    | NOT_STARTED |
    When teacher gets average estimate for group 1
    Then operation is successful
    And average estimation is 11.50

  Scenario: gets average estimate per flow
    Given personal exams are exist
      | examName | studentId | groupId | grade | status      | disciplineId |
      | SQL      | 1         | 1       | 10    | FINISHED    | 1            |
      | SQL      | 2         | 1       | 13.00 | REVIEWED    | 1            |
      | Mongo    | 1         | 2       | 10    | FINISHED    | 1            |
      | SQL      | 3         | 1       | 10    | NOT_STARTED | 1            |
      | SQL      | 1         | 1       | 10    | FINISHED    | 2            |
      | SQL      | 4         | 1       | 13.00 | REVIEWED    | 1            |
    When teacher gets average estimate for the flow 1
    Then operation is successful
    And average estimation is 11.50