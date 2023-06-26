Feature: Topic

  Background:
    Given teacher "Maksym" "Mitkov" with "teacher@domain.com" email exists

  Scenario: system retrieve information list of topic
    Given topics exist
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
    Given "SQL" discipline exists
    And this discipline doesn't have "First" topic
    When teacher create "First" topic
    Then operation is successful

  Scenario: teacher create a topic that already exist
    Given "SQL" discipline exists
    And this discipline has "First" topic
    When teacher creates existing "First" topic
    Then operation should be finished with 409 "Topic with name "First" already exist" error

  Scenario: Retrieve information about topic by id
    Given "SQL" discipline exists
    And this discipline has "Join" topic
    When retrieve information about topic by topicID
    Then operation is successful
    And should return "Join" topic

  Scenario: Retrieve information about topic by id when it doesnt exist
    Given "Mongo" discipline exists
    And this discipline doesn't have "First" topic
    When retrieve information about topic by topicID
    Then operation should be finished with 404 "Topic with id "99" not found" error
