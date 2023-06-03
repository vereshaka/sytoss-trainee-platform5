Feature: Teacher

  Scenario: registration teacher
    Given teacher with "Ivan" firstname and "Ivanov" middlename and "Ivanocich" lastname doesnt exist
    When anonymous register in system as teacher with "Ivan" firstname and "Ivanov" middlename and "Ivanocich" lastname
    Then operation is successful
    And teacher with "Ivan" firstname and "Ivanov" middlename and "Ivanocich" lastname should exist