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
    When a teacher create "Exam" exam with 5 tasks for "SQL" discipline
      | discipline | topic         |
      | SQL        | Set of Tables |
      | SQL        | Join          |
    Then operation is successful
    And "Exam" exam should have 5 tasks for this group
    And "Exam" exam for this group should have topics
      | discipline | topic         |
      | SQL        | Set of Tables |
      | SQL        | Join          |

  Scenario: STP-695 Error occurs during exam creation
    #Given teacher with specific id 4 exists
    Given discipline with specific id 1 and specific teacher id 4 exists
    And task domain with specific id 5 and "task-domain/prod-trade23-db.yml" db, "task-domain/prod-trade23-data.yml" data scripts exists
    And topic with specific id 6 exists
    And task with specific id 3 exists
    When a teacher create exam by request STP-695.json
    Then operation is successful

  Scenario: STP-694 Could not fetch exam list
    Given topics exist
      | discipline | topic  |
      | Mongo      | Select |
      | Mongo      | Join   |
    And "Trade23" task domain with "task-domain/prod-trade23-db.yml" db and "task-domain/prod-trade23-data.yml" data scripts exists for this discipline
      | question                               | answer             | id   | topics       |
      | What are the different subsets of SQL? | select * from dual | *ta1 | Select, Join |
      | "What is content of dual table?        | select * from dual | *ta2 | Select       |
    And this discipline has exams
      | name  | tasks      | taskCount | maxGrade | id   |
      | Exam1 | *ta1, *ta2 | 2         | 2        | *ex1 |
    When a teacher request exam list
    Then operation is successful

# todo AnastasiiaKravchuk: method POST shouldn't allow to update resource
  Scenario: STP-785 Update exam
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
    When teacher updates exam to
      | name  | tasks      | taskCount | maxGrade | id   |
      | Exam2 | *ta1, *ta2 | 3         | 2        | *ex1 |
    Then operation is successful
    And exam is
      | name  | tasks      | taskCount | maxGrade | id   |
      | Exam2 | *ta1, *ta2 | 3         | 2        | *ex1 |

    Scenario: STP-1062 Update exam
      Given disciplines with specific id exist
        | id  | name |
        | *d1 | d1   |
      And groups with specific id exist
        | group | discipline |
        | *g1   | *d1        |
      And exams with specific id exist
        | id   | name | disciplineId |
        | *ex1 | ex1  | *d1          |
      And this exams have assignees
        | id   | examId | relevantFrom               | relevantTo                 |
        | *as1 | *ex1   | 2024-10-27 12:59:00.000000 | 2024-10-28 12:59:00.000000 |
      And this exam assignees have exam assignees to
        | studentId | assigneeId |
        | 1         | *as1       |
      When a teacher updates an exam
        | name  | taskCount | maxGrade | id   |
        | Exam2 | 3         | 2        | *ex1 |
      Then operation is successful