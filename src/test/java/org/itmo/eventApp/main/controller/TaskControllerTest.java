package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.repository.TaskDeadlineTriggerRepository;
import org.itmo.eventapp.main.repository.TaskReminderTriggerRepository;
import org.itmo.eventapp.main.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerTest extends AbstractTestContainers {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskDeadlineTriggerRepository taskDeadlineTriggerRepository;

    @Autowired
    TaskReminderTriggerRepository taskReminderTriggerRepository;

    private UserLoginInfo getUserLoginInfo() {
        UserLoginInfo userDetails = new UserLoginInfo();
        userDetails.setLogin("test_mail@itmo.ru");
        User dummyUser = new User();
        dummyUser.setId(1);
        userDetails.setUser(dummyUser);
        return userDetails;
    }

    @Test
    void taskGetTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role.sql");
        executeSqlScript("/sql/insert_task.sql");

        String expectedTaskJson = """
            {
              "id": 1,
              "event": {
                "eventId":1
              },
              "assignee": {
                "id": 1,
                "name": "test",
                "surname": "user"
              },
              "title": "VERY DIFFICULT TASK",
              "description": "write sql script for tests",
              "taskStatus": "NEW",
              "place": {
                "id": 1,
                "name": "itmo place",
                "address": "itmo university"
              },
              "creationTime": "2025-03-10T21:32:23.536819",
              "deadline": "2025-03-30T21:32:23.536819",
              "reminder": "2025-03-30T21:32:23.536819"
            }
            """;

        mockMvc.perform(get("/api/tasks/1")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));
    }

    @Test
    void taskGetInvalidIdTest() throws Exception {
        mockMvc.perform(get("/api/tasks/-1")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("canGetTask.taskId: Параметр taskId не может быть меньше 1!")));
    }

    @Test
    void taskAddTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role.sql");

        String taskJson = loadAsString("/json/task/taskAdd.json");
        String token = getToken("test_mail@itmo.ru", "password");

        mockMvc.perform(post("/api/tasks")
                .content(taskJson)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().is(201))
            .andExpect(content().string(containsString("1")));

        Task task = taskRepository.findById(1).orElseThrow();

        String newTitle = "CREATED";
        String newDescription = "created";
        TaskStatus newStatus = TaskStatus.NEW;
        LocalDateTime newDeadline = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        LocalDateTime newreminder = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        Integer assigneeId = 1;
        Integer assignerId = 1;

        Assertions.assertAll(
            () -> Assertions.assertEquals(newTitle, task.getTitle()),
            () -> Assertions.assertEquals(newDescription, task.getDescription()),
            () -> Assertions.assertEquals(newStatus, task.getStatus()),
            () -> Assertions.assertNull(task.getPlace()),
            () -> Assertions.assertEquals(newDeadline, task.getDeadline()),
            () -> Assertions.assertEquals(newreminder, task.getReminder()),
            () -> Assertions.assertEquals(assigneeId, task.getAssignee().getId()),
            () -> Assertions.assertEquals(assignerId, task.getAssigner().getId())
        );

        TaskDeadlineTrigger deadlineTrigger = taskDeadlineTriggerRepository.findById(1).orElseThrow();
        TaskReminderTrigger reminderTrigger = taskReminderTriggerRepository.findById(1).orElseThrow();

        Assertions.assertAll(
            () -> Assertions.assertEquals(1, deadlineTrigger.getId()),
            () -> Assertions.assertEquals(newDeadline, deadlineTrigger.getTriggerTime()),
            () -> Assertions.assertEquals(1, reminderTrigger.getId()),
            () -> Assertions.assertEquals(newreminder, reminderTrigger.getTriggerTime())
        );
    }


    @Test
    void taskAddExpiredTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role.sql");

        String taskJson = loadAsString("/json/task/taskAddExpired.json");
        String token = getToken("test_mail@itmo.ru", "password");

        mockMvc.perform(post("/api/tasks")
                .content(taskJson)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().is(201))
            .andExpect(content().string(containsString("1")));

        Task task = taskRepository.findById(1).orElseThrow();

        LocalDateTime newDeadline = LocalDateTime.of(2023, 4, 20, 21, 0, 0);
        LocalDateTime newreminder = LocalDateTime.of(2023, 4, 20, 21, 0, 0);

        Assertions.assertAll(
            () -> Assertions.assertEquals(newDeadline, task.getDeadline()),
            () -> Assertions.assertEquals(newreminder, task.getReminder()),
            () -> Assertions.assertEquals(TaskStatus.EXPIRED, task.getStatus())
        );
    }


    @Test
    void taskAddInvalidTest() throws Exception {


        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role.sql");

        String taskJson = """
            {
              "eventId": -1
            }""";

        mockMvc.perform(post("/api/tasks")
                .content(taskJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isBadRequest());

    }

    // Этот тест пока падает с 403 ошибкой, так как у пользователя нет прав на добавление заданий в event с id 100000
    // Надо подумать что лучше: падать с ошибкой, что такого ивента нет, или с ошибкой, что нет прав на этот ивент
/*    @Test
    void taskAddWithEventNotFoundTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role.sql");

        String taskJson = loadAsString("/json/task/taskAddWithEventNotFound.json");
        String token = getToken("test_mail@itmo.ru", "password");

        mockMvc.perform(post("/api/tasks")
                .content(taskJson)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNotFound());
    }*/

    @Test
    void taskEditTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        executeSqlScript("/sql/insert_task.sql");

        String newTitle = "UPDATED";
        String newDescription = "upd";
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;
        LocalDateTime newDeadline = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        LocalDateTime newreminder = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        Integer assigneeId = 2;
        Integer placeId = 1;

        String taskJson = """
            {
              "eventId": 1,
              "assigneeId": 2,
              "title": "UPDATED",
              "description": "upd",
              "taskStatus": "IN_PROGRESS",
              "placeId": 1,
              "deadline": "2025-04-20T21:00:00",
              "reminder": "2025-04-20T21:00:00"
            }
            """;

        mockMvc.perform(put("/api/tasks/1")
                .content(taskJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk());

        Task edited = taskRepository.findById(1).orElseThrow();
        Assertions.assertAll(
            () -> Assertions.assertEquals(newTitle, edited.getTitle()),
            () -> Assertions.assertEquals(newDescription, edited.getDescription()),
            () -> Assertions.assertEquals(newStatus, edited.getStatus()),
            () -> Assertions.assertEquals(newDeadline, edited.getDeadline()),
            () -> Assertions.assertEquals(newreminder, edited.getReminder()),
            () -> Assertions.assertEquals(assigneeId, edited.getAssignee().getId()),
            () -> {
                Assertions.assertNotNull(edited.getPlace());
                Assertions.assertEquals(placeId, edited.getPlace().getId());
            }

        );

        TaskDeadlineTrigger deadlineTrigger = taskDeadlineTriggerRepository.findById(1).orElseThrow();
        TaskReminderTrigger reminderTrigger = taskReminderTriggerRepository.findById(1).orElseThrow();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, deadlineTrigger.getId()),
                () -> Assertions.assertEquals(newDeadline, deadlineTrigger.getTriggerTime()),
                () -> Assertions.assertEquals(1, reminderTrigger.getId()),
                () -> Assertions.assertEquals(newreminder, reminderTrigger.getTriggerTime())
        );
    }

    @Test
    void taskEditInvalidTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        executeSqlScript("/sql/insert_task.sql");

        String title = "VERY DIFFICULT TASK";
        String description = "write sql script for tests";
        TaskStatus status = TaskStatus.NEW;
        Integer assigneeId = 1;
        Integer placeId = 1;

        String taskJson = """
            {
              "eventId": 1
            }
            """;

        mockMvc.perform(put("/api/tasks/1")
                .content(taskJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isBadRequest());

        Task notEdited = taskRepository.findById(1).orElseThrow();
        Assertions.assertAll(
            () -> Assertions.assertEquals(title, notEdited.getTitle()),
            () -> Assertions.assertEquals(description, notEdited.getDescription()),
            () -> Assertions.assertEquals(status, notEdited.getStatus()),
            () -> Assertions.assertEquals(assigneeId, notEdited.getAssignee().getId()),
            () -> {
                Assertions.assertNotNull(notEdited.getPlace());
                Assertions.assertEquals(placeId, notEdited.getPlace().getId());
            }

        );
    }

    @Test
    void taskDeleteTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        executeSqlScript("/sql/insert_task.sql");

        Assertions.assertTrue(taskRepository.findById(1).isPresent());

        mockMvc.perform(delete("/api/tasks/1")
                .with(user(getUserLoginInfo())))
            .andExpect(status().is(204));

        Assertions.assertFalse(taskRepository.findById(1).isPresent());
    }

    @Test
    void taskSetAssigneeTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        executeSqlScript("/sql/insert_task.sql");

        String expectedTaskJson = """
            {
              "id": 1,
              "assignee": {
                "id": 2,
                "name": "test2",
                "surname": "user2"
              },
              "title": "VERY DIFFICULT TASK",
              "description": "write sql script for tests",
              "taskStatus": "NEW",
              "place": {
                "id": 1,
                "name": "itmo place",
                "address": "itmo university"
              },
              "creationTime": "2025-03-10T21:32:23.536819",
              "deadline": "2025-03-30T21:32:23.536819",
              "reminder": "2025-03-30T21:32:23.536819"
            }
            """;

        mockMvc.perform(put("/api/tasks/1/assignee/2")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));

        Task edited = taskRepository.findById(1).orElseThrow();

        Assertions.assertEquals(2, edited.getAssignee().getId());

        TaskDeadlineTrigger deadlineTrigger = taskDeadlineTriggerRepository.findById(1).orElseThrow();
        TaskReminderTrigger reminderTrigger = taskReminderTriggerRepository.findById(1).orElseThrow();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, deadlineTrigger.getId()),
                () -> Assertions.assertEquals("2025-03-30T21:32:23.536819", deadlineTrigger.getTriggerTime().toString()),
                () -> Assertions.assertEquals(1, reminderTrigger.getId()),
                () -> Assertions.assertEquals("2025-03-30T21:32:23.536819", reminderTrigger.getTriggerTime().toString())
        );
    }


    @Test
    void taskSetAssigneeNotExistingTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        executeSqlScript("/sql/insert_task.sql");

        mockMvc.perform(put("/api/tasks/1/assignee/5")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isNotFound());

        Task edited = taskRepository.findById(1).orElseThrow();

        Assertions.assertEquals(1, edited.getAssignee().getId());

    }

    @Test
    void taskDeleteAssigneeTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        executeSqlScript("/sql/insert_task.sql");

        String expectedTaskJson = """
            {
              "id": 1,
              "assignee": null,
              "title": "VERY DIFFICULT TASK",
              "description": "write sql script for tests",
              "taskStatus": "NEW",
              "place": {
                "id": 1,
                "name": "itmo place",
                "address": "itmo university"
              },
              "creationTime": "2025-03-10T21:32:23.536819",
              "deadline": "2025-03-30T21:32:23.536819",
              "reminder": "2025-03-30T21:32:23.536819"
            }
            """;

        mockMvc.perform(delete("/api/tasks/1/assignee")
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));

        Task task = taskRepository.findById(1).orElseThrow();

        Assertions.assertNull(task.getAssignee());

    }


    @Test
    void taskSetStatusTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        executeSqlScript("/sql/insert_task.sql");


        String expectedTaskJson = """
            {
              "id": 1,
              "assignee": {
                "id": 1,
                "name": "test",
                "surname": "user"
              },
              "title": "VERY DIFFICULT TASK",
              "description": "write sql script for tests",
              "taskStatus": "IN_PROGRESS",
              "place": {
                "id": 1,
                "name": "itmo place",
                "address": "itmo university"
              },
              "creationTime": "2025-03-10T21:32:23.536819",
              "deadline": "2025-03-30T21:32:23.536819",
              "reminder": "2025-03-30T21:32:23.536819"
            }
            """;

        mockMvc.perform(put("/api/tasks/1/status")
                .content("\"IN_PROGRESS\"")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));

        Task task = taskRepository.findById(1).orElseThrow();

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());

    }


    @Test
    void taskSetInvalidStatusTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        executeSqlScript("/sql/insert_task.sql");


        mockMvc.perform(put("/api/tasks/1/status")
                .content("\"WRONG\"")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isBadRequest());

        Task task = taskRepository.findById(1).orElseThrow();

        Assertions.assertEquals(TaskStatus.NEW, task.getStatus());

    }


    @Test
    void taskMoveTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_2.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        //executeSqlScript("/sql/insert_event_role_2.sql");
        executeSqlScript("/sql/insert_task.sql");

        Task task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(1, task.getEvent().getId());

        String expectedTaskJson = """
            [{
              "id": 1,
              "assignee": {
                "id": 1,
                "name": "test",
                "surname": "user"
              },
              "title": "VERY DIFFICULT TASK",
              "description": "write sql script for tests",
              "taskStatus": "NEW",
              "place": {
                "id": 1,
                "name": "itmo place",
                "address": "itmo university"
              },
              "creationTime": "2025-03-10T21:32:23.536819",
              "deadline": "2025-03-30T21:32:23.536819",
              "reminder": "2025-03-30T21:32:23.536819"
            }]
            """;

        mockMvc.perform(put("/api/tasks/event/2")
                .content("[1]")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));

        task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(2, task.getEvent().getId());

    }


    @Test
    void taskWrongMoveTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_3.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        executeSqlScript("/sql/insert_event_role_2.sql");
        executeSqlScript("/sql/insert_task.sql");

        Task task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(1, task.getEvent().getId());

        mockMvc.perform(put("/api/tasks/event/2")
                .content("[1]")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isBadRequest());

        task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(1, task.getEvent().getId());

    }


    @Test
    void taskCopyTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_2.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        //executeSqlScript("/sql/insert_event_role_2.sql");
        executeSqlScript("/sql/insert_task.sql");

        Task task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(1, task.getEvent().getId());

        mockMvc.perform(post("/api/tasks/event/2")
                .content("[1]")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk());

        task = taskRepository.findById(2).orElseThrow();
        Assertions.assertEquals(2, task.getEvent().getId());
        task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(1, task.getEvent().getId());

    }

    @Test
    void taskGetAllInEventTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_2.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        //executeSqlScript("/sql/insert_event_role_2.sql");
        executeSqlScript("/sql/insert_task.sql");
        executeSqlScript("/sql/insert_task_2.sql");
        executeSqlScript("/sql/insert_task_3.sql");
        executeSqlScript("/sql/insert_task_4.sql");

        String expectedTaskJson = """
            [{
              "id": 2,
              "assignee": {
                "id": 1,
                "name": "test",
                "surname": "user"
              },
              "title": "VERY DIFFICULT TASK",
              "description": "write sql script for tests - 2",
              "taskStatus": "EXPIRED",
              "place": {
                "id": 1,
                "name": "itmo place",
                "address": "itmo university"
              },
              "creationTime": "2024-03-10T21:32:23.536819",
              "deadline": "2024-03-30T21:32:23.536819",
              "reminder": "2024-03-30T21:32:23.536819"
            }]
            """;

        String testUrl =
            "/api/tasks/event/1?subEventTasksGet=true&assigneeId=1&assignerId=1&deadlineLowerLimit=2024-03-30T21:00:00&deadlineUpperLimit=2024-03-30T22:00:00";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));

        testUrl =
            "/api/tasks/event/1?subEventTasksGet=true&assigneeId=1&assignerId=1&taskStatus=EXPIRED";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));


        expectedTaskJson = """
            []
            """;

        /*no subtasks*/

        testUrl =
            "/api/tasks/event/1?assigneeId=1&assignerId=1&deadlineLowerLimit=2024-03-30T21:00:00&deadlineUpperLimit=2024-03-30T22:00:00";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));

        /*deadline limits that do not match status*/

        testUrl =
            "/api/tasks/event/1?subEventTasksGet=true&deadlineLowerLimit=2025-03-30T21:00:00&deadlineUpperLimit=2025-03-30T22:00:00&taskStatus=EXPIRED";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));

        /*another assignee id*/

        testUrl =
            "/api/tasks/event/1?subEventTasksGet=true&assigneeId=2&assignerId=1&deadlineLowerLimit=2024-03-30T21:00:00&deadlineUpperLimit=2024-03-30T22:00:00";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));

        /*another assigner id*/

        testUrl =
            "/api/tasks/event/1?subEventTasksGet=true&assigneeId=1&assignerId=2&deadlineLowerLimit=2024-03-30T21:00:00&deadlineUpperLimit=2024-03-30T22:00:00";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));


        expectedTaskJson = """
            [{
              "id": 4,
              "event": {
                "eventId":1,
                "activityId":2
              },
              "assignee": {
                "id": 1,
                "name": "test",
                "surname": "user"
              },
              "title": "VERY DIFFICULT TASK",
              "description": "write sql script for tests - 4",
              "taskStatus": "IN_PROGRESS",
              "place": {
                "id": 1,
                "name": "itmo place",
                "address": "itmo university"
              },
              "creationTime": "2025-03-10T21:32:23.536819",
              "deadline": "2025-03-30T21:32:23.536819",
              "reminder": "2025-03-30T21:32:23.536819"
            }]
            """;

        testUrl =
            "/api/tasks/event/1?subEventTasksGet=true&assignerId=2&personalTasksGet=true";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));


        // assigneeId less important than personalTasksGet param

        testUrl =
                "/api/tasks/event/1?subEventTasksGet=true&assignerId=2&personalTasksGet=true&assigneeId=2";

        mockMvc.perform(get(testUrl)
                        .with(user(getUserLoginInfo())))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedTaskJson));


        expectedTaskJson = """
            []
            """;

        /*no subtasks*/

        testUrl =
            "/api/tasks/event/1?assignerId=2&personalTasksGet=true";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));


    }


    @Test
    void taskGetAllWhereAssigneeTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_2.sql");
        executeSqlScript("/sql/insert_event_role_1.sql");
        //executeSqlScript("/sql/insert_event_role_2.sql");
        executeSqlScript("/sql/insert_task.sql");
        executeSqlScript("/sql/insert_task_2.sql");
        executeSqlScript("/sql/insert_task_3.sql");
        executeSqlScript("/sql/insert_task_4.sql");

        String expectedTaskJson = """
            [{
              "id": 4,
              "event": {
                "eventId":1,
                "activityId":2
              },
              "assignee": {
                "id": 1,
                "name": "test",
                "surname": "user"
              },
              "title": "VERY DIFFICULT TASK",
              "description": "write sql script for tests - 4",
              "taskStatus": "IN_PROGRESS",
              "place": {
                "id": 1,
                "name": "itmo place",
                "address": "itmo university"
              },
              "creationTime": "2025-03-10T21:32:23.536819",
              "deadline": "2025-03-30T21:32:23.536819",
              "reminder": "2025-03-30T21:32:23.536819"
            }]
            """;

        String testUrl =
            "/api/tasks/where-assignee?taskStatus=IN_PROGRESS";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));


        expectedTaskJson = """
            []
            """;


        testUrl =
            "/api/tasks/where-assignee?taskStatus=IN_PROGRESS&eventId=1";

        mockMvc.perform(get(testUrl)
                .with(user(getUserLoginInfo())))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedTaskJson));

    }
}
