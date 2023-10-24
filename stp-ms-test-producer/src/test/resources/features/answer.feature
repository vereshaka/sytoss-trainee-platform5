Feature: Answer
  Background:
    Given student "John" "Do" with "student@domain.com" email exists

  Scenario: student answers on task
    Given this student has personal exam with id *1 with 60 max grade and sum of coef 30 exist
      | answerId | taskId | taskDomainId | question                    | answer              | status      | etalon                 | grade | coef | comment          |
      | 1        | 12     | 22           | get all from tasks table    | SELECT * FROM tasks | GRADED      | SELECT * FROM tasks    | 1     | 1    | "answer correct" |
      | 2        | 13     | 23           | get all from students table |                     | IN_PROGRESS | SELECT * FROM students |       | 1    |                  |
      | 3        | 14     | 24           | get all from tests table    |                     | NOT_STARTED | SELECT * FROM tests    |       | 3    |                  |
      | 4        | 15     | 25           | get all from houses table   |                     | NOT_STARTED | SELECT * FROM houses   |       | 5    |                  |
      | 5        | 16     | 26           | get all from orders table   |                     | NOT_STARTED | SELECT * FROM orders   |       | 1    |                  |
    When student calls answer with value "SELECT * FROM students" on personal exam with id *1
    Then operation is successful
    And answer with id 2 of personal exam with id *1 should have value "SELECT * FROM students" and change status to GRADED and grade 2
    And response should return next task with number 3
