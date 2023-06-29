Feature: Group

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists

  @Bug
  Scenario: System retrieve all groups by discipline
     #Given this teacher has "SQL" discipline with id *1 contains "AT-11, AT-12, TT-12"
    #And  this teacher has "SQL" discipline with id *2 contains "AT-13"
    #When receive all groups by discipline with id *1
    Given groups exist
      | discipline | group |
      | 1          | 11    |
      | 1          | 12    |
      | 2          | 13    |
    When receive all groups by discipline with id 1
    Then operation is successful
    And groups should received
      | discipline | group |
      | 1          | 11    |
      | 1          | 12    |

  @Bug
  Scenario: Create group
    Given "SQL" discipline exists
    And "AT-21-2" group does not exist for this discipline
    When teacher "Maksym" "Mitkov" create "AT-21-2" group for "SQL" discipline
    Then operation is successful
    And "AT-21-2" group should be for "SQL" discipline

    @Bug
  Scenario: Create group when group exist
    Given "SQL" discipline exists
    And "AT-21-2" group exist for this discipline
    When teacher "Maksym" "Mitkov" create "AT-21-2" group for "SQL" discipline that exists
    Then operation should be finished with 409 "Group with name "AT-21-2" already exist" error

  Scenario: get teacher's groups
    Given teachers have groups
      | teacherId | discipline | group|
      | *4        | Mongo      | 11   |
      | *4        | Mongo      | 12   |
      | *5        | Java       | 13   |
      | *4        | Mongo      | 14   |
      | *4        | SQL        | 21   |
    When receive all groups by teacher with id *4
    Then operation is successful
    And groups should received
      | group   | discipline |
      | 11      | 1          |
      | 12      | 2          |
      | 14      | 3          |
      | 21      | 4          |