Feature: Group

  Scenario: retrieve discipline information
    Given "SQL" discipline exists
    When receive "SQL" discipline information
    Then operation is successful
    And "SQL" discipline