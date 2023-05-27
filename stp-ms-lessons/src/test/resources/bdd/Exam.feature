Feature: Exam

  Scenario: the system receives a save exam request
    Given group exists in DB
      | groupId |  name  |
      |    1    | JAVA-1 |
    When a request to save the exam calls
      | relevantFrom | relevantTo | duration | groupId | numberOfTasks |
      |  22.05.2022  | 24.05.2022 |    20    |    1    |       15      |
    And exam has topics in discipline SQL
      | topicName               |
      | Set of Tables           |
      | Data Types              |
      | Primary and Foreign Key |
    Then operation is successful
