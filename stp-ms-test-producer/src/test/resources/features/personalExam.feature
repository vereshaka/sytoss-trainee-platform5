Feature: PersonalExam

  Background:
    Given Student "John" "Do" with "student@domain.com" email exists
    And Teacher "John" "Teacher" with "sytoss@gmail.com" email exists

  Scenario: system create personal exam
    Given tasks exist
      | discipline | topic           | task       |
      | SQL        | Join            | Inner Join |
      | SQL        | Join            | Left Join  |
      | SQL        | Sorting results | SELECT     |
      | Java       | Sorting results | SELECT     |
    When system create "First" personal exam with maxGrade 4 by "SQL" discipline and "Join" topic with 2 tasks for student with 1 id between 11.05.2024 and 12.05.2024
    Then operation is successful
    And "First" exam by "SQL" discipline and "Join" topic for student with 1 id should have tasks
      | task       |
      | Inner Join |
      | Left Join  |

  Scenario: Student can start test
    Given this student has "Exam" personal exam and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    When this student start personal exam "Exam"
    Then operation is successful
    And should return personal exam with time more than 10 and amountOfTasks 1
    And PersonalExam with "What are the different subsets of SQL?" question should be received
    And status of "Exam" exam for this student should be "IN_PROGRESS"
    And status of first answer of "Exam" exam for this student should be "IN_PROGRESS"

  Scenario: Student is not allowed to start test when it already start
    Given this student has "Exam" personal exam and IN_PROGRESS status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    When this student start second time personal exam "Exam"
    Then operation is successful

  Scenario: Student is not allowed to start test when it already finished
    Given this student has "Exam" personal exam and FINISHED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    When this student start second time personal exam "Exam"
    Then operation should be finished with 409 "{"message":"Exam is finished!","technical":true}" error

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

  Scenario: system create personal exam with a photo retrieve
    Given tasks exist
      | discipline | topic           | task       |
      | SQL        | Join            | Inner Join |
      | SQL        | Join            | Left Join  |
      | SQL        | Sorting results | SELECT     |
      | Java       | Sorting results | SELECT     |
    When system create "First" personal exam with maxGrade 4 by "SQL" discipline and "Join" topic with 2 tasks for student with 1 id between 11.05.2024 and 12.05.2024
    Then operation is successful
    And "First" exam by "SQL" discipline and "Join" topic for student with 1 id should have tasks
      | task       |
      | Inner Join |
      | Left Join  |

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
    Given Student "John" "Doo" with "student2@domain.com" email exists with id 13
    And personal "Exam1" exam with examId 1 for student with 13 id and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    And personal "Exam2" exam with examId 1 for student with 13 id and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    When operation for retrieving personal exams for this student
    Then operation is successful
    And operation should return 2 personal exams
      | Exam1 |
      | Exam2 |

  Scenario: Should receive error when student retrieve all personal exams by studentId
    Given personal "Exam1" exam with examId 1 for student with 3 id and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    And personal "Exam2" exam with examId 1 for student with 3 id and NOT_STARTED status exist and time 10 and amountOfTasks 1
      | task                                   | task status | script |
      | What are the different subsets of SQL? | NOT_STARTED | .uml   |
    When operation for retrieving personal exams for userId 3 was called
    Then operation should be finished with 403 "<null>" error

  Scenario: teacher gets system and teacher grade
    Given student 1 has personal exam with id 123abc123 and exam name "SQL exam" and date 11.05.2023
      | listOfSubjects | taskId | question                    | answer                    | task status | grade |
      | DML            | 1      | get all from tasks table    | SELECT * FROM tasks       | GRADED      | 1     |
      | DDl            | 2      | get all from students table | SELECT * FROM students    | GRADED      | 1     |
      | DQL            | 3      | get all from tests table    | SELECT * FROM programmers | GRADED      | 0     |
      | DDl            | 4      | get all from houses table   | SELECT * FROM trees       | GRADED      | 0     |
      | DML            | 5      | get all from orders table   | SELECT * FROM orders      | GRADED      | 1     |
    And this teacher review personal exam with id 123abc123 and exam name "SQL exam" with such grades
      | taskId | teacherGrade |
      | 1      | 2            |
      | 2      | 2            |
      | 3      | 0            |
      | 4      | 0            |
      | 5      | 2            |
    When the exam with id 123abc123 is reviewed
    Then operation is successful
    And system grade should be 3
    And teacher grade should be 6

  Scenario: get personalExam by discipline id,exam assignees and students list
    Given personal exams exist
      | personalExamId | disciplineId | examAssigneeId | studentId | groupId | summaryGrade | startDate           |
      | *pe1           | *d1          | *ea1           | 11         | *g1     | 6            | 30-11-2023T11:55:00 |
      | *pe2           | *d1          | *ea2           | 12         | *g2     | 7            | 30-11-2023T11:55:00 |
      | *pe3           | *d1          | *ea1           | 13         | *g1     | 11           | 30-11-2023T11:55:00 |
      | *pe4           | *d1          | *ea2           | 14         | *g2     | 20           | 30-11-2023T11:55:00 |
      | *pe5           | *d2          | *ea3           | 14         | *g3     | 21           | 30-11-2023T11:55:00 |
      | *pe6           | *d2          | *ea3           | 15         | *g3     | 11           | 30-11-2023T11:55:00 |
    When the teacher gets personal exam by discipline *d1, exam assignees *ea1,*ea2 and students 11,12,14
    Then operation is successful
    And personal exams are
      | personalExamId | disciplineId | examAssigneeId | studentId | groupId | summaryGrade | startDate           |
      | *pe1           | *d1          | *ea1           | 11         | *g1     | 6            | 30-11-2023T11:55:00 |
      | *pe2           | *d1          | *ea2           | 12         | *g2     | 7            | 30-11-2023T11:55:00 |
      | *pe4           | *d1          | *ea2           | 14         | *g2     | 20           | 30-11-2023T11:55:00 |
