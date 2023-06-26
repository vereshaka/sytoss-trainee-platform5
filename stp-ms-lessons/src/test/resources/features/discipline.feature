Feature: Discipline

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists

  Scenario: teacher creates a new discipline
    Given discipline "SQL" doesn't exist
    When teacher creates "SQL" discipline
    Then operation is successful
    And "SQL" discipline should exist

  Scenario: teacher creates a discipline that already exists
    Given "SQL" discipline exists
    When teacher creates existing "SQL" discipline
    Then operation should be finished with 409 "Discipline with name "SQL" already exist" error

  Scenario: retrieve discipline information
    Given "SQL" discipline exists
    When receive "SQL" discipline information
    Then operation is successful
    And "SQL" discipline should be received

  Scenario: get teacher's discipline
    Given disciplines exist
      | teacher | discipline |
      | 1       | SQL        |
      | 1       | Mongo      |
      | 2       | SQL        |
      | 1       | H2         |
    When teacher with id 1 retrieve his disciplines
    Then operation is successful
    And disciplines should be received
      | teacher | discipline |
      | 1       | SQL        |
      | 1       | Mongo      |
      | 1       | H2         |

  Scenario: search disciplines
    Given disciplines exist
      | discipline |
      | SQL        |
      | Mongo      |
    When receive all disciplines
    Then operation is successful
    And disciplines should be received
      | discipline |
      | SQL        |
      | Mongo      |

  Scenario: Link task to topic
    Given "SQL" discipline exists
    When link this discipline to group with id 17
    Then operation is successful