Feature: Exam

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists

  Scenario: STP-xxx Assign groups to exam
    Given topics exist
      | discipline | topic  |
      | SQL        | Select |
      | SQL        | Join   |
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