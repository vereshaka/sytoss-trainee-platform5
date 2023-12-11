Feature: Discipline

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists
    And analytics is empty

  Scenario: receive all discipline of student
    Given disciplines exist
      | discipline |
      | first      |
      | second     |
      | third      |
    And groups exist
      | discipline | group |
      | first      | 11    |
      | second     | 12    |
      | third      | 13    |
    When student receive his disciplines
    Then operation is successful
    And should receive information about discipline of student
      | discipline |
      | first      |
      | second     |
      | third      |

  Scenario: teacher creates a new discipline
    Given discipline "SQL" doesn't exist
    When teacher creates "SQL" discipline
    Then operation is successful
    And "SQL" discipline should exist

  Scenario: teacher creates a discipline that already exists
    Given "SQL" discipline exists
    When teacher creates "SQL" discipline
    Then operation should be finished with 409 "Discipline with name "SQL" already exist" error

  Scenario: retrieve discipline information
    Given "SQL" discipline exists
    When receive "SQL" discipline information
    Then operation is successful
    And "SQL" discipline should be received

  Scenario: get teacher's discipline
    Given disciplines exist
      | teacherId | discipline |
      | 1         | SQL        |
      | 1         | Mongo      |
      | 2         | SQL        |
      | 1         | H2         |
    When teacher with id 1 retrieve his disciplines
    Then operation is successful
    And disciplines should be received
      | teacherId | discipline |
      | 1         | H2         |
      | 1         | Mongo      |
      | 1         | SQL        |

  Scenario: get disciplines in order
    Given disciplines exist
      | teacherId | discipline | creationDate |
      | 1         | SQL        | 2021-10-31   |
      | 1         | Mongo      | 2022-10-31   |
      | 2         | SQL        | 2020-10-31   |
      | 1         | H2         | 2020-10-31   |
    When teacher with id 1 retrieve his disciplines
    Then operation is successful
    And disciplines should be received
      | teacherId | discipline |
      | 1         | Mongo      |
      | 1         | SQL        |
      | 1         | H2         |

  Scenario: search disciplines
    Given disciplines exist
      | discipline |
      | SQL        |
      | Mongo      |
    When receive all disciplines
    Then operation is successful
    And disciplines should be received
      | discipline |
      | SQL        |
      | Mongo      |

  Scenario: Link task to topic
    Given "SQL" discipline exists
    When link this discipline to group with id 17
    Then operation is successful

  Scenario: get discipline's icon
    Given "SQL" discipline exists
    And this discipline has icon with bytes "1, 2, 3"
    When receive this discipline's icon
    Then operation is successful
    And discipline's icon should be received

  Scenario: STP-772 Delete Discipline
    Given "SQL-772" discipline exists with id *d1
    And topics exist
      | discipline | topic  |
      | SQL-772    | Select |
      | SQL-772    | Join   |
    And "Trade23" task domain with "task-domain/prod-trade23-db.yml" db and "task-domain/prod-trade23-data.yml" data scripts exists for this discipline
      | question                               | answer            | id  | topics       |
      | What are the different subsets of SQL? | select  from dual | ta1 | Select, Join |
      | "What is content of dual table?        | select  from dual | ta2 | Select       |
    And this discipline has exams
      | name  | tasks    | topic  | taskCount | maxGrade | id   |
      | Exam1 | ta1, ta2 | Select | 2         | 2        | *ex1 |
    And this exams have assignees
      | relevantFrom               | relevantTo                 | examId | id   |
      | 2023-10-27 12:59:00.000000 | 2023-10-28 12:59:00.000000 | *ex1   | *as1 |
    And this exam assignees have exam assignees to
      | studentId | assigneeId |
      | 1         | *as1       |
    When a teacher delete discipline with id *d1
    Then operation is successful

  Scenario: STP-993 Retrieve exams by discipline for student
    Given "SQL-993" discipline exists with id *d1
    And topics exist
      | discipline | topic  |
      | SQL-993    | Select |
      | SQL-993    | Join   |
    And "Trade23" task domain with "task-domain/prod-trade23-db.yml" db and "task-domain/prod-trade23-data.yml" data scripts exists for this discipline
      | question                               | answer            | id  | topics       |
      | What are the different subsets of SQL? | select  from dual | ta1 | Select, Join |
      | "What is content of dual table?        | select  from dual | ta2 | Select       |
    And this discipline has exams
      | name  | tasks    | topic  | taskCount | maxGrade | id   |
      | Exam1 | ta1, ta2 | Select | 2         | 2        | *ex1 |
      | Exam2 | ta1, ta2 | Select | 2         | 2        | *ex2 |
    And this exams have assignees
      | relevantFrom               | relevantTo                 | examId | id   |
      | 2023-10-27 12:59:00.000000 | 2023-10-28 12:59:00.000000 | *ex1   | *as1 |
      | 2023-10-27 12:59:00.000000 | 2023-10-28 12:59:00.000000 | *ex2   | *as2 |
      | 2023-10-27 12:59:00.000000 | 2023-10-28 12:59:00.000000 | *ex2   | *as3 |
    And this exam assignees have exam assignees to
      | studentId | assigneeId | groupId |
      | 0         | *as1       |         |
      |           | *as2       | 1       |
      |           | *as3       | 2       |
    And student has such groups
      | studentId | groupId |
      | 0         | 1       |
      | 0         | 2       |
    When a student gets exams for this discipline
    Then operation is successful
    And exams should be
      | name  | id   |
      | Exam1 | *ex1 |
      | Exam2 | *ex2 |
