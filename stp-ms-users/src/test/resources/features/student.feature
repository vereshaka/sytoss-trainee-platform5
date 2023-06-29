Feature: Student

  Background:
    Given student with "FirstName" firstName and "LastName" lastName and "test1@gmail.com" email exists

  Scenario: receive all groups of student
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

  Scenario: retrieve photo
    Given this user has profile photo
    When retrieve photo of this user
    Then operation is successful
    And should return photo