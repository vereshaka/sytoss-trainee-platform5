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

  Scenario: get user's icon
    Given student with "Ivan" firstName and "Ivanovich" lastName and "test@gmail.com" email exists
    And this student has photo with bytes "1, 2, 3"
    When receive this student's photo
    Then operation is successful
    And student's photo should be received