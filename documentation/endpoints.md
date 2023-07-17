# ms-check-task

regexp ~\/api\/(task\/check.*)$

POST    /api/task/check <br>
POST    /api/task/check-etalon <br>
POST    /api/task/check-request <br>

# ms-image-provider
regexp ~\/api\/(image\/.*)$

POST    /api/image/convert <br>
GET     /api/image/question/{question_image_id} <br>

# ms-test-producer
regexp ~\/api\/((personal-exam|test)\/.*)$

POST    /api/personal-exam/create <br>
GET     /api/personal-exam/{id}/summary <br>
POST    /api/personal-exam/{personalExamId}/task/answer <br>
GET     /api/test/{personalExamId}/start <br>
POST    /api/test/{personalExamId}/answer <br>
GET     /api/test/is-used-now/task-domain/{taskDomainId}/ <br>

# ms-users
regexp ~\/api\/((user|group)(\/(.*)))$
regexp ~\/api\/group$

GET     /api/user/me <br>
POST    /api/user/me <br>
GET     /api/user/me/groups <br>
GET     /api/user/me/photo <br>
GET     /api/user/me/groupsId <br>

GET     /api/user/{userId}/photo <br>

POST    /api/group/{groupId}/student/{studentId} <br>
POST    /api/group <br>

# ms-lessons
regexp ~\/api\/(.*)$

GET     /api/disciplines <br>
GET     /api/disciplines/my <br>

POST    /api/discipline <br>
POST    /api/discipline/{disciplineId}/topic <br>
GET     /api/discipline/{disciplineId}/groups <br>
GET     /api/discipline/{disciplineId} <br>
POST    /api/discipline/{disciplineId} <br>
GET     /api/discipline/{disciplineId}/task-domains <br>
POST    /api/discipline/{disciplineId}/group/{groupId} <br>
GET     /api/discipline/{disciplineId}/icon <br>
GET     /api/discipline/{disciplineId}/topics <br>

GET     /api/topic/{topicId} <br>
GET     /api/topic/{topicId}/tasks <br>
GET     /api/topic/{topicId}/icon <br>

GET     /api/task/{taskId} <br>
POST    /api/task <br>
PUT     /api/task/{taskId}/condition/{conditionId} <br>
PUT     /api/task/{taskId}/condition <br>
POST    /api/task/{taskId}/topic/{topicId} <br>

GET     /api/task-domain/{taskDomainId} <br>
PUT     /api/task-domain/{taskDomainId} <br>
PUT     /api/task-domain/{taskDomainId}/puml <br>


GET     /api/my/groups <br>             
GET     /api/exam/save



