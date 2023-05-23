Feature: check answer

  Scenario: Check student`s answer
    Given database with script: '{"databaseChangeLog": [{"changeSet": {"id": "create_answer","author": "ivan-larin","changes": [{"createTable": {"tableName": "Answer","columns": [{"column": {"name": "Answer","type": "varchar"}}]}},{"createTable": {"tableName": "Etalon","columns": [{"column": {"name": "Etalon","type": "varchar"}}]}}]}},{"changeSet": {"id": "insert-answer","changes": [{"insert": {"columns": [{"column": {"name": "Answer","value": "it_is_answer"}}],"tableName": "Answer"}},{"insert": {"columns": [{"column": {"name": "Etalon","value": "it_is_etalon}}],"tableName": "Etalon"}}]}}]}"'
    When student's answer is checking with "select * from answer", "select * from etalon"
    Then answer and etalon should be got from database as QueryResult
    And database should be dropped