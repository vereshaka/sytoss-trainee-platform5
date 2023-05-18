Feature: PersonalExam

  Scenario: Student can start test
    Given personal exam exist
      | studentId | exam name | exam status | question                               | task status |
      | 2         | Exam      | NOT_STARTED | What are the different subsets of SQL? | NOT_STARTED |
    When student with 2 id start personal exam "Exam"
    Then operation is successful
    And should return "What are the different subsets of SQL?" question
    And status of "Exam" exam for student with 2 id should be "IN_PROGRESS"
    And status of first answer of "Exam" exam for student with 2 id should be "IN_PROGRESS"

  Scenario: Student is not allowed to start test when it already start
    Given personal exam exist
      | studentId | exam name | exam status | question                               | task status |
      | 2         | Exam      | IN_PROGRESS | What are the different subsets of SQL? | IN_PROGRESS |
    When student with 2 id start personal exam "Exam"
    Then operation should be finished with 409 "Exam is already in progress!" error