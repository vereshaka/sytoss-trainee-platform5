Feature: Student

  Scenario: update photo
    Given student with "Ivan" firstName and "Ivanov" middleName and "Ivanovich" lastName and "test@gmail.com" email exists
    When photo of student with email "test@gmail.com" is updated
    Then operation is successful
#    And teacher with "Ivan" firstname and "Ivanov" middlename and "Ivanovich" lastname should exist