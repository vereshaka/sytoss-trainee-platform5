Feature: check answer

  Scenario: Check student's answer
    Given database with script: "{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}"
    When student's answer is checking with "select * from answer", "select * from etalon"
    Then answer should be "it_is_answer"
    And etalon should be "it_is_etalon"
    And answer and etalon should have same number of columns and rows
    When database drop is initiated
    Then database should be dropped