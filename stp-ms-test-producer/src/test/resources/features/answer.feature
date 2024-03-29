Feature: Answer

  Background:
    Given Student "John" "Do" with "student@domain.com" email exists

  Scenario: student answers on task
    Given this student has personal exam with id *1 with 60 max grade and sum of coef 30 exist
      | answerId | taskId | taskDomainId | question                      | answer              | status      | etalon                 | grade | coef | comment          |
      | 1        | 12     | 22           | get all from task table       | SELECT * FROM tasks | GRADED      | SELECT * FROM tasks    | 1     | 1    | "answer correct" |
      | 2        | 13     | 23           | get all from discipline table |                     | IN_PROGRESS | SELECT * FROM discipline |       | 1    |                  |
      | 3        | 14     | 24           | get all from topic table      |                     | NOT_STARTED | SELECT * FROM topic    |       | 3    |                  |
    When student calls answer with value "SELECT * FROM discipline" on personal exam with id *1
    Then operation is successful
    And answer with id 2 of personal exam with id *1 should have value "SELECT * FROM discipline" and change status to ANSWERED
    And response should return next task with number 3
