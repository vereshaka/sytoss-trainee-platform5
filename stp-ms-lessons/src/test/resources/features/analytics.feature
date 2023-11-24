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
    And updated analytic element should be
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent |
      | *d1          | *ex1   | 1         | *pe1           | 12    | 1         |

  Scenario: migration
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | examAssigneeId | startDate           |
      | *d11         | *ex11  | 1         | *pe11          | 10    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 2         | *pe12          | 12    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 3         | *pe13          | 11    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 4         | *pe14          | 8     | *ea11          | 30-11-2023T11:55:00 |
    And personal exams for migration exist
      | personalExamId | examAssigneeId | studentId | summaryGrade | disciplineId | startDate           |
      | *pe11          | *ea11          | 1         | 6            | *d11         | 30-11-2023T11:55:00 |
      | *pe12          | *ea11          | 2         | 7            | *d11         | 30-11-2023T11:55:00 |
      | *pe13          | *ea11          | 3         | 11           | *d11         | 30-11-2023T11:55:00 |
      | *pe14          | *ea11          | 4         | 20           | *d11         | 30-11-2023T11:55:00 |
      | *pe15          | *ea11          | 5         | 21           | *d11         | 30-11-2023T11:55:00 |
    When teacher makes a migration for discipline *d11
    Then operation is successful
    And analytics elements should be
      | disciplineId | examId | studentId | personalExamId | grade | examAssigneeId | startDate           |
      | *d11         | *ex11  | 1         | *pe11          | 6     | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 2         | *pe12          | 7     | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 3         | *pe13          | 11    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 4         | *pe14          | 20    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 5         | *pe15          | 21    | *ea11          | 30-11-2023T11:55:00 |

  Scenario: get ratings by id
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent |
      | *d1          | *ex1   | 1         | *pe1           | 6     | 1         |
      | *d1          | *ex2   | 2         | *pe1           | 5     | 3         |
      | *d2          | *ex1   | 1         | *pe1           | 7     | 4         |
      | *d2          | *ex2   | 1         | *pe1           | 6     | 2         |
    When teacher gets ratings by discipline *d1, by exam null and by group null
    Then operation is successful
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 1         | 6        | 1            | 1    |
      | 2         | 5        | 3            | 2    |

  Scenario: get ratings by id and exam id
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent |
      | *d1          | *ex1   | 1         | *pe1           | 6     | 1         |
      | *d1          | *ex1   | 2         | *pe1           | 5     | 3         |
      | *d2          | *ex1   | 1         | *pe1           | 7     | 4         |
      | *d2          | *ex1   | 2         | *pe1           | 6     | 2         |
    When teacher gets ratings by discipline *d1, by exam *ex1 and by group null
    Then operation is successful
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 1         | 6        | 1            | 1    |
      | 2         | 5        | 3            | 2    |

  Scenario: get ratings by id and group id
    Given discipline with id *d1 exists
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
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 1         | 7        | 3            | 1    |

  Scenario: get ratings by id, exam id and group id
    Given discipline with id *d1 exists
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
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 1         | 10       | 3            | 1    |
      | 2         | 5        | 3            | 2    |


  Scenario: When teacher requests students analytics then response with detailed information has to be returned
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent |
      | *d12         | *ex12  | 1         | *pe16          | 1     | 1         |
      | *d12         | *ex13  | 1         | *pe17          | 5     | 4         |
      | *d12         | *ex14  | 1         | *pe18          | 15    | 5         |
      | *d12         | *ex12  | 2         | *pe19          | 3     | 1         |
      | *d12         | *ex13  | 2         | *pe21          | 6     | 4         |
      | *d12         | *ex14  | 2         | *pe22          | 12    | 5         |
    When teacher requests analytics for discipline *d12 and student 1
    Then operation is successful
    And StudentDisciplineStatistic object has to be returned
      | disciplineId | studentId | average grade | average spent time | max grade | min spent time |
      | *d12         | 1         | 7             | 3                  | 15        | 1              |
    And StudentDisciplineStatistic should has tests
      | examId | examName | examMaxGrade | personalExamGrade | personalExamSpentTime | personalExamId |
      | *ex12  | Exam 1   | 3            | 1                 | 1                     | *pe16          |
      | *ex13  | Exam 1   | 6            | 5                 | 4                     | *pe17          |
      | *ex14  | Exam 1   | 15           | 15                | 5                     | *pe18          |

  Scenario: When teacher requests summary by discipline then response with detailed information has to be returned
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent |
      | *d13         | *ex12  | 1         | *pe16          | 1     | 1         |
      | *d13         | *ex13  | 1         | *pe17          | 5     | 4         |
      | *d13         | *ex14  | 1         | *pe18          | 15    | 5         |
      | *d13         | *ex12  | 2         | *pe19          | 3     | 1         |
      | *d13         | *ex13  | 2         | *pe21          | 6     | 4         |
      | *d13         | *ex14  | 2         | *pe22          | 12    | 5         |
    When teacher requests discipline summary for discipline *d13
    Then operation is successful
