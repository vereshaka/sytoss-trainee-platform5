Feature: Teacher

  Scenario: registration teacher
    Given teacher with "Ivan" firstName and "Ivanovich" lastName doesnt exist
    When anonymous register in system as teacher with "Ivan" firstname and "Ivanovich" lastname
    Then operation is successful
    And teacher with "Ivan" firstname and "Ivanovich" lastname should exist
