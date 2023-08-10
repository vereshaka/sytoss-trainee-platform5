Feature: PersonalExam

  @Bug @STP-409
  Scenario: system create personal exam
    Given tasks exist
      | discipline | topic           | task       |
      | SQL        | Join            | Inner Join |
      | SQL        | Join            | Left Join  |
      | SQL        | Sorting results | SELECT     |
      | Java       | Sorting results | SELECT     |
    When system create "First" personal exam by "SQL" discipline and "Join" topic with 2 tasks for student with 1 id
    Then operation is successful
    And "First" exam by "SQL" discipline and "Join" topic for student with 1 id should have tasks
      | task       |
      | Inner Join |
      | Left Join  |

  Scenario: Student can start test
    Given personal "Exam" exam for student with 2 id and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    When student with 2 id start personal exam "Exam"
    Then operation is successful
    And should return personal exam with time 10 and amountOfTasks 1
    And PersonalExam with "What are the different subsets of SQL?" question should be received
    And status of "Exam" exam for student with 2 id should be "IN_PROGRESS"
    And status of first answer of "Exam" exam for student with 2 id should be "IN_PROGRESS"

  Scenario: Student is not allowed to start test when it already start
    Given personal "Exam" exam for student with 2 id and IN_PROGRESS status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    When student with 2 id start second time personal exam "Exam"
    Then operation should be finished with 409 "Exam is already in progress!" error

  Scenario: Student is not allowed to start test when
    Given personal "Exam" exam for student with 2 id and IN_PROGRESS status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script | taskDomainId |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   | 1            |
    And personal "Exam" exam for student with 2 id and FINISHED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script | taskDomainId |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   | 1            |
    When system check task domain with id: "1" is used
    Then operation is successful
    And should return "true"

  @Bug @STP-409
  Scenario: system create personal exam with a photo retrieve
    Given tasks exist
      | discipline | topic           | task       |
      | SQL        | Join            | Inner Join |
      | SQL        | Join            | Left Join  |
      | SQL        | Sorting results | SELECT     |
      | Java       | Sorting results | SELECT     |
    When system create "First" personal exam by "SQL" discipline and "Join" topic with 2 tasks for student with 1 id
    Then operation is successful
    And "First" exam by "SQL" discipline and "Join" topic for student with 1 id should have tasks
      | task       |
      | Inner Join |
      | Left Join  |
    And tasks from exam should have id image

  Scenario: retrieve all personal exams by examId
    Given personal "Exam1" exam with examId 1 for student with 3 id and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    And personal "Exam2" exam with examId 1 for student with 4 id and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    When operation for retrieving personal exams for examId 1 was called
    Then operation is successful
    And operation should return 2 personal exams
      | Exam1 |
      | Exam2 |

  Scenario: retrieve all personal exams by examId
    Given personal "Exam1" exam with examId 1 for student with 3 id and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    And personal "Exam2" exam with examId 1 for student with 3 id and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    When operation for retrieving personal exams for userId 3 was called
    Then operation is successful
    And operation should return 2 personal exams
      | Exam1 |
      | Exam2 |