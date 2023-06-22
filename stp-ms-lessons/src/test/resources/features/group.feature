Feature: Group

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists

  @Bug
  Scenario: System retrieve all groups by discipline
     #Given this teacher has "SQL" discipline with id *1 contains "AT-11, AT-12, TT-12"
    #And  this teacher has "SQL" discipline with id *2 contains "AT-13"
    #When receive all groups by discipline with id *1

    Given this teacher has "SQL" discipline with id *1 contains groups with id
      | id |
      | 11 |
      | 12 |
    And this teacher has "Java" discipline with id *2 contains groups with id
      | id |
      | 13 |
    When receive all groups by discipline with id *1
    Then operation is successful


#    Given groups exist
#      | discipline | group |
#      | 1          | 11    |
#      | 1          | 12    |
#      | 2          | 13    |
#    When receive all groups by discipline with id 1
#    Then operation is successful
#    And groups should received
#      | discipline | group |
#      | 1          | 11    |
#      | 1          | 12    |