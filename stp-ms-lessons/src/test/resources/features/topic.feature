Feature: Topic

  Background:
    Given teacher "Maksym" "Mitkov" exists

  Scenario: system retrieve information list of topic
    Given topic exist
      | discipline | topic           |
      | SQL        | Join            |
      | SQL        | Join Inner      |
      | SQL        | Sorting results |
      | SQL        | Drop results    |
    When system retrieve information about topics by discipline
    Then operation is successful
    And should return
      | discipline | topic           |
      | SQL        | Join            |
      | SQL        | Join Inner      |
      | SQL        | Sorting results |
      | SQL        | Drop results    |

  Scenario: teacher create a new topic
    Given disciplines exist
      | disciplineId | discipline   | teacherId |
      | 1            | SQL          |  7        |
      | 2            | Mongo        |  7        |
    And "First" topic by "Mongo" discipline doesn't exist
    When teacher create "First" topic
    Then operation is successful

  Scenario: teacher create a topic that already exist
    Given topic exist
      | discipline | topic |
      | Mongo      | First |
    When teacher creates existing "First" topic
    Then operation should be finished with 409 "Topic with name "First" already exist" error

  Scenario: Retrieve information about topic by id
    Given "SQL" discipline exists
    And This discipline has "Join" topic
    When retrieve information about topic by topicID
    Then operation is successful
    And should return "Join" topic

  Scenario: Retrieve information about topic by id when it doesnt exist
    Given "Mongo" discipline exists
    And "First" topic by "Mongo" discipline doesn't exist
    When retrieve information about topic by topicID
    Then operation should be finished with 404 "Topic with id "99" not found" error