Feature: Summary

  Background:
    Given teacher "John" "Do" with "teacher@domain.com" email exists

  Scenario: teacher path to summary when all tasks are graded
    Given student 1 has personal exam with id 123abc123 and exam name "SQL exam" and date 11.05.2023
      | listOfSubjects | taskId | question                    | answer                    | task status | grade | comment          |
      | DML            | 1      | get all from tasks table    | SELECT * FROM tasks       | GRADED      | 1     | answer correct   |
      | DDl            | 2      | get all from students table | SELECT * FROM students    | GRADED      | 1     | answer correct   |
      | DQL            | 3      | get all from tests table    | SELECT * FROM programmers | GRADED      | 0     | answer uncorrect |
      | DDl            | 4      | get all from houses table   | SELECT * FROM trees       | GRADED      | 0     | answer uncorrect |
      | DML            | 5      | get all from orders table   | SELECT * FROM orders      | GRADED      | 1     | answer correct   |
    When the exam with id 123abc123 is done
    Then operation is successful
    And summary grade should be 3
    And response should return "SQL exam" personal exam for student 1 from 11.05.2023
      | question                    | answer                    | task status | grade | comment          |
      | get all from tasks table    | SELECT * FROM tasks       | GRADED      | 1     | answer correct   |
      | get all from students table | SELECT * FROM students    | GRADED      | 1     | answer correct   |
      | get all from tests table    | SELECT * FROM programmers | GRADED      | 0     | answer uncorrect |
      | get all from houses table   | SELECT * FROM trees       | GRADED      | 0     | answer uncorrect |
      | get all from orders table   | SELECT * FROM orders      | GRADED      | 1     | answer correct   |


  Scenario: student path to summary when not all tasks are graded
    Given student 1 has personal exam with id 123abc123 and exam name "SQL exam" and date 11.05.2023
      | listOfSubjects | taskId | question                    | answer                    | task status | grade | comment          |
      | DML            | 1      | get all from tasks table    | SELECT * FROM tasks       | GRADED      | 1     | answer correct   |
      | DDl            | 2      | get all from students table | SELECT * FROM students    | GRADED      | 1     | answer correct   |
      | DQL            | 3      | get all from tests table    | SELECT * FROM programmers | GRADED      | 0     | answer uncorrect |
      | DDl            | 4      | get all from houses table   | SELECT * FROM trees       | GRADED      | 0     | answer uncorrect |
      | DML            | 5      | get all from orders table   | SELECT * FROM orders      | GRADED      | 0     | answer uncorrect |
    When student path to summary to exam with id 123abc123
    Then operation should be finished with 403 error
