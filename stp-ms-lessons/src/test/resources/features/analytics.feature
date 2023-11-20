Feature: Analytics

  Background:
    Given teacher "Teacher" "1" with "teacher@domain.com" email exists

  Scenario: update analytics
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent |
      | *d1          | *ex1   | 1         | *pe1           | 10    | 1         |
    And teacher changes grade to
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent |
      | *d1          | *ex1   | 1         | *pe1           | 12    | 1         |
    When teacher updates analytics element
    Then operation is successful
    And grade equals 12

  Scenario: migration
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | examAssigneeId | startDate           |
      | *d1          | *ex1   | 1         | *pe1           | 10    | *ea1           | 30-11-2023T11:55:00 |
      | *d1          | *ex1   | 2         | *pe2           | 12    | *ea1           | 30-11-2023T11:55:00 |
      | *d1          | *ex1   | 3         | *pe3           | 11    | *ea1           | 30-11-2023T11:55:00 |
      | *d1          | *ex1   | 4         | *pe4           | 8     | *ea1           | 30-11-2023T11:55:00 |
    And personal exams for migration exist
      | personalExamId | examAssigneeId | studentId | summaryGrade | disciplineId | startDate           |
      | *pe1           | *ea1           | 1         | 6            | *d1          | 30-11-2023T11:55:00 |
      | *pe2           | *ea1           | 2         | 7            | *d1          | 30-11-2023T11:55:00 |
      | *pe3           | *ea1           | 3         | 11           | *d1          | 30-11-2023T11:55:00 |
      | *pe4           | *ea1           | 4         | 20           | *d1          | 30-11-2023T11:55:00 |
      | *pe5           | *ea1           | 5         | 21           | *d1          | 30-11-2023T11:55:00 |
    When teacher makes a migration for discipline *d1
    Then operation is successful
    And analytics elements should be
      | disciplineId | examId | studentId | personalExamId | grade | examAssigneeId | startDate           |
      | *d1          | *ex1   | 1         | *pe1           | 6     | *ea1           | 30-11-2023T11:55:00 |
      | *d1          | *ex1   | 2         | *pe2           | 7     | *ea1           | 30-11-2023T11:55:00 |
      | *d1          | *ex1   | 3         | *pe3           | 11    | *ea1           | 30-11-2023T11:55:00 |
      | *d1          | *ex1   | 4         | *pe4           | 20    | *ea1           | 30-11-2023T11:55:00 |
      | *d1          | *ex1   | 5         | *pe5           | 21    | *ea1           | 30-11-2023T11:55:00 |

  Scenario: get ratings by id
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent |
      | *d1          | *ex1   | 1         | *pe1           | 6     | 1         |
      | *d1          | *ex2   | 2         | *pe1           | 5     | 3         |
      | *d2          | *ex1   | 1         | *pe1           | 7     | 4         |
      | *d2          | *ex2   | 1         | *pe1           | 6     | 2         |
    When teacher gets ratings by discipline *d1, by exam null and by group null
    Then operation is successful
    And rating models should be
      | studentId | avgGrade | avgTimeSpent |
      | 2         | 5        | 3            |
      | 1         | 6        | 1            |

  Scenario: get ratings by id and exam id
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent |
      | *d1          | *ex1   | 1         | *pe1           | 6     | 1         |
      | *d1          | *ex1   | 2         | *pe1           | 5     | 3         |
      | *d2          | *ex1   | 1         | *pe1           | 7     | 4         |
      | *d2          | *ex1   | 1         | *pe1           | 6     | 2         |
    When teacher gets ratings by discipline *d1, by exam *ex1 and by group null
    Then operation is successful
    And rating models should be
      | studentId | avgGrade | avgTimeSpent |
      | 2         | 5        | 3            |
      | 1         | 6        | 1            |

  Scenario: get ratings by id and group id
    Given discipline with specific id *d1 exists
    And groups with specific id exist
      | discipline | group |
      | *d1        | 1     |
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | groupId |
      | *d1          | *ex1   | 1         | *pe1           | 10    | 3         | 1       |
      | *d1          | *ex2   | 2         | *pe1           | 5     | 3         | 2       |
      | *d1          | *ex4   | 1         | *pe2           | 5     | 4         | 1       |
      | *d1          | *ex3   | 1         | *pe1           | 6     | 2         | 1       |
    When teacher gets ratings by discipline *d1, by exam null and by group 1
    Then operation is successful
    And rating models should be
      | studentId | avgGrade | avgTimeSpent |
      | 1         | 7        | 3            |

  Scenario: get ratings by id, exam id and group id
    Given discipline with specific id *d1 exists
    And groups with specific id exist
      | discipline | group |
      | *d1        | 1     |
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | groupId |
      | *d1          | *ex1   | 1         | *pe1           | 10    | 3         | 1       |
      | *d1          | *ex1   | 2         | *pe1           | 5     | 3         | 1       |
      | *d1          | *ex1   | 3         | *pe1           | 5     | 4         | 2       |
      | *d1          | *ex1   | 4         | *pe1           | 6     | 2         | 2       |
      | *d1          | *ex2   | 1         | *pe2           | 10    | 3         | 1       |
      | *d1          | *ex2   | 2         | *pe2           | 5     | 3         | 1       |
      | *d1          | *ex2   | 3         | *pe2           | 5     | 4         | 2       |
      | *d1          | *ex2   | 4         | *pe2           | 6     | 2         | 2       |
    When teacher gets ratings by discipline *d1, by exam *ex1 and by group 1
    Then operation is successful
    And rating models should be
      | studentId | avgGrade | avgTimeSpent |
      | 2         | 5        | 3            |
      | 1         | 10       | 3            |