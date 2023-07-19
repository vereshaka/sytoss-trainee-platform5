Feature: Group

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists

  Scenario: System retrieve all groups by discipline
    Given teacher has "SQL" discipline with id *d1 contains groups with id "11, 12"
    And teacher has "Java" discipline with id *d2 contains groups with id "13"
    When receive all groups by discipline with id *d1
    Then operation is successful
    And groups of discipline with id *d1 should be received
      | group |
      | 11    |
      | 12    |

  Scenario: get teacher's groups
    Given teachers have groups
      | teacherId | discipline | group |
      | *4        | Mongo      | 11    |
      | *4        | Mongo      | 12    |
      | *5        | Java       | 13    |
      | *4        | Mongo      | 14    |
      | *4        | SQL        | 21    |
    When receive all groups by teacher with id *4
    Then operation is successful
    And groups should be received
      | group | discipline |
      | 11    | 1          |
      | 12    | 2          |
      | 14    | 3          |
      | 21    | 4          |
