Feature: Answer

  Background:
    Given teacher "John" "Teacher" with "sytoss@gmail.com" email exists

  Scenario: gets average estimate per group
    Given personal exams are exist
      | examName | studentId | groupId | grade | status      |
      | SQL      | 1         | 1       | 10    | FINISHED    |
      | SQL      | 2         | 1       | 13.02 | REVIEWED    |
      | Mongo    | 1         | 2       | 10    | FINISHED    |
      | SQL      | 3         | 1       | 10    | NOT_STARTED |
    When teacher gets average estimate for group 1
    Then operation is successful
    And average estimation is 11.51