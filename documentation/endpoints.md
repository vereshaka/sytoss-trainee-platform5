# ms-check-task
POST    /api/task/check <br>
POST    /api/task/check-etalon <br>
POST    /api/task/check-request <br>

# ms-image-provider
POST    /api/convert/image <br>
GET     /api/question-image/{question_image_id} <br>

# ms-lessons
POST    /api/discipline/{disciplineId}/topic <br>
GET     /api/discipline/{disciplineId}/groups <br>
GET     /api/discipline/{disciplineId} <br>
POST    /api/discipline/{disciplineId} <br>
GET     /api/discipline/{disciplineId}/taskDomains <br>
GET     /api/disciplines <br>
POST    /api/discipline/{disciplineId}/group/{groupId} <br>
GET     /api/my/disciplines <br>
GET     /api//discipline/{disciplineId}/icon <br>

GET     /api/exam/save

GET     /api/task/{taskId} <br>
POST    /api/task/ <br>
PUT     /api/task/{taskId}/condition/{conditionId} <br>
PUT     /api/task/{taskId}/condition <br>

GET     /api/taskDomain/{taskDomainId} <br>
PUT     /api/taskDomain/{taskDomainId} <br>
PUT     /api/taskDomain/{taskDomainId}/puml <br>

POST    /api/discipline <br>
GET     /api/my/disciplines <br>
GET     /api/my/groups <br>             

GET     /api/discipline/{disciplineId}/topics <br>
GET     /api/topic/{topicId} <br>
POST    /api/task/{taskId}/topic/{topicId} <br>
GET     /api/topic/{topicId}/tasks <br>
GET     /api/topic/{topicId}/icon <br>

# ms-test-producer
POST    /api/personalExam/create <br>
GET     /api/test/{personalExamId}/start <br>
GET     /api/personalExam/{id}/summary <br>
GET     /api/taskDomain/{taskDomainId}/isUsedNow <br>
POST    /api/personalExam/{personalExamId}/task/answer <br>

# ms-users
GET     /api/user/me <br>
POST    /api/user/me <br>
GET     /api/user/my/groups <br>
GET     /api/{userId}/photo <br>
GET     /api/me/photo <br>
GET     /api/my/groupsId <br>

POST    /api/group/ <br>
POST    /api/group/{groupId}/student/{studentId} <br>