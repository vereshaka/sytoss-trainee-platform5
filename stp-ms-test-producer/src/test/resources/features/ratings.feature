Feature: Answer

  Background:
    Given teacher "John" "Teacher" with "sytoss@gmail.com" email exists

  Scenario: gets average estimate per group
    Given personal exams are exist
      | examName | studentId | groupId | grade | status      | examAssigneeId | examId |
      | SQL      | 1         | 1       | 10    | FINISHED    | 1              | 1      |
      | SQL      | 2         | 1       | 13.02 | REVIEWED    | 2              | 1      |
      | Mongo    | 1         | 2       | 10    | FINISHED    | 1              | 1      |
      | SQL      | 3         | 1       | 10    | NOT_STARTED | 2              | 1      |
    When teacher gets average estimate for group 1
    Then operation is successful
    And average estimation is 11.51