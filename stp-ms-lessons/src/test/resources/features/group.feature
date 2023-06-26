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