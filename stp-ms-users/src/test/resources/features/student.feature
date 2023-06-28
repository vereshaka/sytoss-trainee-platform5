Feature: Student

  Scenario: receive all groups of student
    Given student with "FirstName" firstName and "LastName" lastName and "test1@gmail.com" email exists
    And this student assign to group
      | group        |
      | group first  |
      | group second |
      | group third  |
    When student receive his groups
    Then operation is successful
    And should receive information about group of student
      | group        |
      | group first  |
      | group second |
      | group third  |

  Scenario: receive all discipline of student
    Given student with "FirstName" firstName and "LastName" lastName and "test1@gmail.com" email exists
    And this student assign to group
      | group        |
      | group first  |
      | group second |
    When student receive his disciplines
      | discipline |
      | first      |
      | second     |
      | third      |
    Then operation is successful
    And should receive information about discipline of student
      | discipline |
      | first      |
      | second     |
      | third      |
      | first      |
      | second     |
      | third      |