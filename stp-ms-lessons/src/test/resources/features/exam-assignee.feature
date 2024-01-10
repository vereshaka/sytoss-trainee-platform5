Feature: Exam Assignee

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists

  Scenario: STP-xxx Assign groups to exam
    Given topics exist
      | discipline | topic  |
      | Mongo      | Select |
      | Mongo      | Join   |
    And "Trade23" task domain with "task-domain/prod-trade23-db.yml" db and "task-domain/prod-trade23-data.yml" data scripts exists for this discipline
      | question                               | answer             | id   | topics       |
      | What are the different subsets of SQL? | select * from dual | *ta1 | Select, Join |
      | "What is content of dual table?        | select * from dual | *ta2 | Select       |
    And this discipline has assigned groups: 1,2
    And this discipline has exams
      | name  | tasks      | taskCount | maxGrade | id   |
      | Exam1 | *ta1, *ta2 | 2         | 2        | *ex1 |
    When a teacher assign *ex1 exam to groups: 1
    Then operation is successful

  Scenario: STP-1002 Unassign exam
    Given topics exist
      | discipline | topic  |
      | Mongo      | Select |
      | Mongo      | Join   |
    And "Trade23" task domain with "task-domain/prod-trade23-db.yml" db and "task-domain/prod-trade23-data.yml" data scripts exists for this discipline
      | question                               | answer             | id   | topics       |
      | What are the different subsets of SQL? | select * from dual | *ta1 | Select, Join |
      | "What is content of dual table?        | select * from dual | *ta2 | Select       |
    And this discipline has assigned groups: 1,2
    And this discipline has exams
      | name  | tasks      | taskCount | maxGrade | id   |
      | Exam1 | *ta1, *ta2 | 2         | 2        | *ex1 |
    And this exams have assignees
      | id   | examId | relevantFrom               | relevantTo                 |
      | *ea1 | *ex1   | 2023-10-27 12:59:00.000000 | 2023-10-28 12:59:00.000000 |
    And this exam assignees have exam assignees to
      | assigneeId | groupId |
      | *ea1       | 1       |
      | *ea1       | 1       |
      | *ea1       | 1       |
    When delete exam assignee with id *ea1
    Then operation is successful
    And there is no exam assignee with id *ea1