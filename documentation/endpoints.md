# ms-check-task
POST    /api/task/check
POST    /api/task/check-etalon

# ms-lessons
POST    /api/discipline/{disciplineId}/topic
GET     /api/discipline/{disciplineId}/groups
GET     /api/discipline/{disciplineId}
POST    /api/discipline/{disciplineId}
GET     /api/discipline/{disciplineId}/taskDomains
GET     /api/disciplines
POST    /api/discipline/{disciplineId}/group/{groupId}

GET     /api/exam/save

GET     /api/task/{taskId}
POST    /api/task/
PUT     /api/{taskId}/condition/{conditionId}
PUT     /api/{taskId}/condition

GET     /api/taskDomain/{taskDomainId}
PUT     /api/taskDomain/{taskDomainId}
PUT     /api/taskDomain/{taskDomainId}/puml

POST    /api/teacher/{teacherId}/discipline
GET     /api/my/disciplines
GET     /api/my/groups                 ------------------------- in progress

GET     /api/discipline/{disciplineId}/topics
GET     /api/topic/{topicId}
POST    /api/task/{taskId}/topic/{topicId}
GET     /api/topic/{topicId}/tasks

# ms-test-producer
POST    /api/personalExam/create
GET     /api/test/{personalExamId}/start
GET     /api/personalExam/{id}/summary
GET     /api/taskDomain/{taskDomainId}/isUsedNow
POST    /api/personalExam/{personalExamId}/task/answer

# ms-users

GET     /api/user/me
POST    /api/user/me
GET     /api/user/my/groups

POST    /api/group/
POST    /api/{groupId}/student/{studentId}
