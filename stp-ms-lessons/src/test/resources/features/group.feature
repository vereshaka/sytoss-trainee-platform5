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
      | SQL        | AT-11 |
      | SQL        | AT-12 |
      | Java       | AT-13 |
      | SQL        | TT-12 |
    When receive all groups by "SQL" discipline
    Then operation is successful
    And groups should received
      | discipline | group |
      | SQL        | AT-11 |
      | SQL        | AT-12 |
      | SQL        | TT-12 |

  Scenario: Create group
    Given "SQL" discipline exists
    And "AT-21-2" group does not exist for this discipline
    When teacher "Maksym" "Mitkov" create "AT-21-2" group for "SQL" discipline
    Then operation is successful
    And "AT-21-2" group should be for "SQL" discipline

  Scenario: Create group when group exist
    Given "SQL" discipline exists
    And "AT-21-2" group exist for this discipline
    When teacher "Maksym" "Mitkov" create "AT-21-2" group for "SQL" discipline that exists
    Then operation should be finished with 409 "Group with name "AT-21-2" already exist" error

  Scenario: get teacher's groups
    And "SQL" discipline exists for this teacher
    And groups exist
      | discipline | group |
      | SQL        | AT-11 |
      | SQL        | AT-12 |
      | Java       | AT-13 |
      | Oracle     | TT-12 |
    When teacher with lastname "Mitkov" retrieve all his groups
    Then operation is successful
    And groups should received
      | group |
      | AT-11 |
      | AT-12 |