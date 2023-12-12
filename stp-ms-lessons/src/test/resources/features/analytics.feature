Feature: Analytics

  Background:
    Given teacher "Teacher" "1" with "teacher@domain.com" email exists

  Scenario: update analytics
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 10    | 1         | *ea1           |
    And teacher changes grade to
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 12    | 1         | *ea1           |
    When teacher updates analytics element
    Then operation is successful
    And updated analytic element should be
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 12    | 1         | *ea1           |

  Scenario: migration
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | examAssigneeId | startDate           |
      | *d11         | *ex11  | 1         | *pe11          | 10    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 2         | *pe12          | 12    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 3         | *pe13          | 11    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 4         | *pe14          | 8     | *ea11          | 30-11-2023T11:55:00 |
    And personal exams for migration exist
      | personalExamId | examAssigneeId | studentId | summaryGrade | disciplineId | startDate           | examId | status   |
      | *pe11          | *ea11          | 1         | 6            | *d11         | 30-11-2023T11:55:00 | *ex11  | REVIEWED |
      | *pe12          | *ea11          | 2         | 7            | *d11         | 30-11-2023T11:55:00 | *ex11  | REVIEWED |
      | *pe13          | *ea11          | 3         | 11           | *d11         | 30-11-2023T11:55:00 | *ex11  | REVIEWED |
      | *pe14          | *ea11          | 4         | 20           | *d11         | 30-11-2023T11:55:00 | *ex11  | REVIEWED |
      | *pe15          | *ea11          | 5         | 21           | *d11         | 30-11-2023T11:55:00 | *ex11  | REVIEWED |
      | *pe16          | *ea12          | 5         | 11           | *d12         | 30-11-2023T11:55:00 | *ex12  | REVIEWED |
    When teacher makes a migration for discipline *d11
    Then operation is successful
    And analytics elements should be
      | disciplineId | examId | studentId | personalExamId | grade | examAssigneeId | startDate           |
      | *d11         | *ex11  | 1         | *pe11          | 6     | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 2         | *pe12          | 7     | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 3         | *pe13          | 11    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 4         | *pe14          | 20    | *ea11          | 30-11-2023T11:55:00 |
      | *d11         | *ex11  | 5         | *pe15          | 21    | *ea11          | 30-11-2023T11:55:00 |

  Scenario: migration2
    Given disciplines with specific id exist
      | id  | name |
      | *d1 | d1   |
      | *d2 | d2   |
    And groups with specific id exist
      | group | discipline |
      | *g1   | *d1        |
      | *g2   | *d1        |
      | *g3   | *d2        |
    And exams with specific id exist
      | id   | name | disciplineId |
      | *ex1 | ex1  | *d1          |
      | *ex2 | ex3  | *d1          |
      | *ex3 | ex3  | *d2          |
    And this exams have assignees
      | id   | examId | relevantFrom               | relevantTo                 |
      | *ea1 | *ex1   | 2023-10-27 12:59:00.000000 | 2023-10-28 12:59:00.000000 |
      | *ea2 | *ex2   | 2023-10-27 12:59:00.000000 | 2023-10-28 12:59:00.000000 |
      | *ea3 | *ex3   | 2023-10-27 12:59:00.000000 | 2023-10-28 12:59:00.000000 |
    And analytics elements exist
      | disciplineId | examId | examAssigneeId | personalExamId | grade | startDate           | studentId | groupId |
      | *d1          | *ex1   | *ea1           | *pe1           | 10    | 30-11-2023T11:55:00 | 1         | *g1     |
      | *d1          | *ex1   | *ea1           | *pe2           | 12    | 30-11-2023T11:55:00 | 2         | *g2     |
      | *d1          | *ex1   | *ea1           | *pe3           | 11    | 30-11-2023T11:55:00 | 3         | *g1     |
      | *d1          | *ex1   | *ea2           | *pe4           | 8     | 30-11-2023T11:55:00 | 4         | *g1     |
    And personal exams for migration exist
      | personalExamId | disciplineId | examAssigneeId | studentId | groupId | summaryGrade | startDate           | status      |
      | *pe1           | *d1          | *ea1           | 1         | *g1     | 0            | 30-11-2023T11:55:00 | NOT_STARTED |
      | *pe2           | *d1          | *ea2           | 2         | *g2     | 7            | 30-11-2023T11:55:00 | REVIEWED    |
      | *pe3           | *d1          | *ea1           | 3         | *g1     | 11           | 30-11-2023T11:55:00 | FINISHED    |
      | *pe4           | *d1          | *ea2           | 4         | *g2     | 0            | 30-11-2023T11:55:00 | IN_PROGRESS |
      | *pe5           | *d2          | *ea3           | 4         | *g3     | 21           | 30-11-2023T11:55:00 | FINISHED    |
      | *pe6           | *d2          | *ea3           | 5         | *g3     | 11           | 30-11-2023T11:55:00 | REVIEWED    |
    When teacher makes a migration for discipline *d1
    Then operation is successful
    And analytics elements should be
      | disciplineId | examId | examAssigneeId | personalExamId | grade | startDate           | studentId | groupId |
      | *d1          | *ex1   | *ea1           |                |       |                     | 1         | *g1     |
      | *d1          | *ex1   | *ea1           |                |       |                     | 3         | *g1     |
      | *d1          | *ex1   | *ea1           |                |       |                     | 2         | *g2     |
      | *d1          | *ex1   | *ea1           |                |       |                     | 4         | *g2     |
      | *d1          | *ex2   | *ea2           | *pe2           | 7     | 30-11-2023T11:55:00 | 2         | *g2     |
      | *d1          | *ex2   | *ea2           |                |       |                     | 4         | *g2     |
      | *d1          | *ex2   | *ea2           |                |       |                     | 1         | *g1     |
      | *d1          | *ex2   | *ea2           |                |       |                     | 3         | *g1     |

  Scenario: When teacher requests students analytics then response with detailed information has to be returned
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d12         | *ex12  | 1         | *pe16          | 1     | 1         | *ea1           |
      | *d12         | *ex13  | 1         | *pe17          | 5     | 4         | *ea2           |
      | *d12         | *ex14  | 1         | *pe18          | 15    | 5         | *ea3           |
      | *d12         | *ex12  | 2         | *pe19          | 3     | 1         | *ea1           |
      | *d12         | *ex13  | 2         | *pe21          | 6     | 4         | *ea2           |
      | *d12         | *ex14  | 2         | *pe22          | 12    | 5         | *ea3           |
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
    And groups with specific id exist
      | discipline | group |
      | *d1        | 1     |
      | *d1        | 2     |
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | groupId | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 10    | 3         | 1       | *ea1           |
      | *d1          | *ex1   | 2         | *pe2           | 5     | 3         | 2       | *ea1           |
      | *d1          | *ex2   | 1         | *pe3           | 5     | 4         | 1       | *ea2           |
      | *d1          | *ex2   | 2         | *pe4           | 4     | 2         | 2       | *ea2           |
    When teacher requests discipline summary for discipline *d1
    Then operation is successful
    And full discipline summary should has values
      | average grade | average spent time | max grade | min spent time |
      | 6             | 3                  | 10        | 2              |
    And exam summaries by groups should be
      | exam id | group id | max grade | students average grade | students average spent time | max students grade | min students spent time |
      | *ex1    | 1        | 10        | 10                     | 3                           | 10                 | 3                       |
      | *ex1    | 2        | 5         | 5                      | 3                           | 5                  | 3                       |
      | *ex2    | 1        | 5         | 5                      | 4                           | 5                  | 4                       |
      | *ex2    | 2        | 4         | 4                      | 2                           | 4                  | 2                       |

  Scenario: When teacher requests summary by discipline and group then response with detailed information has to be returned
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d13         | *ex12  | 1         | *pe16          | 1     | 1         | *ea1           |
      | *d13         | *ex13  | 1         | *pe17          | 5     | 4         | *ea2           |
      | *d13         | *ex14  | 1         | *pe18          | 15    | 5         | *ea3           |
      | *d13         | *ex12  | 2         | *pe19          | 3     | 1         | *ea1           |
      | *d13         | *ex13  | 2         | *pe21          | 6     | 4         | *ea2           |
      | *d13         | *ex14  | 2         | *pe22          | 12    | 5         | *ea3           |
    When teacher requests discipline summary for group with id 1 and discipline with id *d13
    Then operation is successful
    And discipline summary should has values
      | average grade | average spent time | max grade | min spent time |
      | 7             | 3                  | 15        | 1              |
    And exam summaries should be
      | exam id | max grade | students average grade | students average spent time | max students grade | min students spent time |
      | *ex12   | 3         | 2                      | 1                           | 3                  | 1                       |
      | *ex13   | 6         | 5.5                    | 4                           | 6                  | 4                       |
      | *ex14   | 15        | 13.5                   | 5                           | 15                 | 5                       |

  Scenario: avg: get ratings by id
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 6     | 1         | *ea1           |
      | *d1          | *ex2   | 2         | *pe1           | 5     | 3         | *ea2           |
      | *d2          | *ex1   | 1         | *pe1           | 7     | 4         | *ea1           |
      | *d2          | *ex2   | 1         | *pe1           | 6     | 2         | *ea2           |
    When user gets ratings by discipline *d1, by exam null and by group null and grade type average
    Then operation is successful
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 1         | 6        | 1            | 1    |
      | 2         | 5        | 3            | 2    |

  Scenario: avg: get ratings by id and exam id
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d3          | *ex5   | 1         | *pe1           | 6     | 1         | *ea1           |
      | *d3          | *ex5   | 2         | *pe2           | 10    | 3         | *ea1           |
      | *d4          | *ex5   | 1         | *pe1           | 7     | 4         | *ea2           |
      | *d4          | *ex5   | 2         | *pe2           | 6     | 2         | *ea2           |
    When user gets ratings by discipline *d3, by exam *ex5 and by group null and grade type average
    Then operation is successful
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 2         | 10       | 3            | 1    |
      | 1         | 6        | 1            | 2    |

  Scenario: avg: get ratings by id and group id
    Given discipline with id *d1 exists
    And groups with specific id exist
      | discipline | group |
      | *d1        | 1     |
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | groupId | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 10    | 3         | 1       | *ea1           |
      | *d1          | *ex2   | 2         | *pe1           | 5     | 3         | 2       | *ea2           |
      | *d1          | *ex4   | 1         | *pe2           | 5     | 4         | 1       | *ea4           |
      | *d1          | *ex3   | 1         | *pe1           | 6     | 2         | 1       | *ea3           |
    When user gets ratings by discipline *d1, by exam null and by group 1 and grade type average
    Then operation is successful
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 1         | 7        | 3            | 1    |

  Scenario: avg: get ratings by id, exam id and group id
    Given discipline with id *d1 exists
    And groups with specific id exist
      | discipline | group |
      | *d1        | 1     |
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | groupId | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 10    | 3         | 1       | *ea1           |
      | *d1          | *ex1   | 2         | *pe1           | 5     | 3         | 1       | *ea1           |
      | *d1          | *ex1   | 3         | *pe1           | 5     | 4         | 2       | *ea1           |
      | *d1          | *ex1   | 4         | *pe1           | 6     | 2         | 2       | *ea1           |
      | *d1          | *ex2   | 1         | *pe2           | 10    | 3         | 1       | *ea2           |
      | *d1          | *ex2   | 2         | *pe2           | 5     | 3         | 1       | *ea2           |
      | *d1          | *ex2   | 3         | *pe2           | 5     | 4         | 2       | *ea2           |
      | *d1          | *ex2   | 4         | *pe2           | 6     | 2         | 2       | *ea2           |
    When user gets ratings by discipline *d1, by exam *ex1 and by group 1 and grade type average
    Then operation is successful
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 1         | 10       | 3            | 1    |
      | 2         | 5        | 3            | 2    |

  Scenario: sum: get ratings by id
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 6     | 1         | *ea1           |
      | *d1          | *ex1   | 2         | *pe2           | 5     | 3         | *ea1           |
      | *d1          | *ex2   | 1         | *pe1           | 6     | 1         | *ea2           |
      | *d1          | *ex2   | 2         | *pe2           | 8     | 5         | *ea2           |
      | *d2          | *ex1   | 1         | *pe1           | 7     | 4         | *ea2           |
      | *d2          | *ex1   | 2         | *pe1           | 6     | 2         | *ea2           |
    When user gets ratings by discipline *d1, by exam null and by group null and grade type summary
    Then operation is successful
    And ratings should be
      | studentId | sumGrade | sumTimeSpent | rank |
      | 2         | 13       | 8            | 1    |
      | 1         | 12       | 2            | 2    |

  Scenario: sum: get ratings by id and exam id
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d3          | *ex5   | 1         | *pe1           | 6     | 1         | *ea1           |
      | *d3          | *ex5   | 2         | *pe2           | 10    | 3         | *ea1           |
      | *d4          | *ex5   | 1         | *pe1           | 7     | 4         | *ea2           |
      | *d4          | *ex5   | 2         | *pe2           | 6     | 2         | *ea2           |
    When user gets ratings by discipline *d3, by exam *ex5 and by group null and grade type summary
    Then operation is successful
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 2         | 10       | 3            | 1    |
      | 1         | 6        | 1            | 2    |

  Scenario: sum: get ratings by id and group id
    Given discipline with id *d1 exists
    And groups with specific id exist
      | discipline | group |
      | *d1        | 1     |
      | *d1        | 2     |
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | groupId | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 10    | 3         | 1       | *ea1           |
      | *d1          | *ex2   | 1         | *pe2           | 5     | 3         | 1       | *ea2           |
      | *d1          | *ex3   | 1         | *pe3           | 5     | 4         | 1       | *ea3           |
      | *d1          | *ex4   | 2         | *pe4           | 6     | 2         | 2       | *ea4           |
      | *d1          | *ex1   | 2         | *pe1           | 6     | 2         | 1       | *ea1           |
      | *d1          | *ex2   | 2         | *pe2           | 5     | 2         | 2       | *ea2           |
    When user gets ratings by discipline *d1, by exam null and by group 1 and grade type summary
    Then operation is successful
    And ratings should be
      | studentId | sumGrade | sumTimeSpent | rank |
      | 1         | 20       | 10           | 1    |

  Scenario: sum: get ratings by id, exam id and group id
    Given discipline with id *d1 exists
    And groups with specific id exist
      | discipline | group |
      | *d1        | 1     |
      | *d1        | 2     |
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | groupId | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 10    | 3         | 1       | *ea1           |
      | *d1          | *ex1   | 2         | *pe2           | 6     | 2         | 1       | *ea1           |
      | *d1          | *ex2   | 1         | *pe1           | 5     | 3         | 1       | *ea2           |
      | *d1          | *ex2   | 3         | *pe1           | 5     | 2         | 2       | *ea2           |
      | *d1          | *ex4   | 3         | *pe2           | 6     | 2         | 2       | *ea4           |
    When user gets ratings by discipline *d1, by exam *ex1 and by group 1 and grade type summary
    Then operation is successful
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 1         | 10       | 3            | 1    |
      | 2         | 6        | 2            | 2    |

  Scenario: student: get ratings by id when personal exam id is null
    Given analytics elements exist
      | disciplineId | examId | studentId | personalExamId | grade | timeSpent | examAssigneeId |
      | *d1          | *ex1   | 1         | *pe1           | 6     | 1         | *ea1           |
      | *d1          | *ex2   | 1         | *pe2           | 5     | 3         | *ea2           |
      | *d1          | *ex1   | 3         |                | 0     | 0         | *ea1           |
      | *d1          | *ex2   | 4         | *pe3           | 6     | 2         | *ea2           |
    When user gets ratings by discipline *d1, by exam null and by group null and grade type average
    Then operation is successful
    And ratings should be
      | studentId | avgGrade | avgTimeSpent | rank |
      | 4         | 6        | 2            | 1    |
      | 1         | 5.5      | 2            | 2    |
