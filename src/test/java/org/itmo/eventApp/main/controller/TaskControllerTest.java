package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest extends AbstractTestContainers {
    @Autowired
    TaskRepository taskRepository;

    @Test
    void taskGetTest() throws Exception {
        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
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
                  "taskStatus": "NEW",
                  "place": {
                    "id": 1,
                    "name": "itmo place",
                    "address": "itmo university"
                  },
                  "creationTime": "2025-03-10T21:32:23.536819",
                  "deadline": "2025-03-30T21:32:23.536819",
                  "notificationDeadline": "2025-03-30T21:32:23.536819"
                }
                """;

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedTaskJson));
    }

    @Test
    void taskGetInvalidIdTest() throws Exception {
        mockMvc.perform(get("/api/tasks/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("taskGet.id: Параметр id не может быть меньше 1!")));
    }

    @Test
    void taskAddTest() throws Exception {

        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");

        String newTitle = "CREATED";
        String newDescription = "created";
        TaskStatus newStatus = TaskStatus.NEW;
        LocalDateTime newDeadline = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        LocalDateTime newNotificationDeadline = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        Integer assigneeId = 1;
        /*TODO: assigner id check after adding security*/

        String taskJson = """
                {
                  "eventId": 1,
                  "assignee": {
                    "id": 1,
                    "name": "test",
                    "surname": "user"
                  },
                  "title": "CREATED",
                  "description": "created",
                  "taskStatus": "NEW",
                  "place": null,
                  "deadline": "2025-04-20T21:00:00",
                  "notificationDeadline": "2025-04-20T21:00:00"
                }""";

        mockMvc.perform(post("/api/tasks")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(content().string(containsString("1")));

        Task task = taskRepository.findById(1).orElseThrow();
        Assertions.assertAll(
                () -> Assertions.assertEquals(task.getTitle(), newTitle),
                () -> Assertions.assertEquals(task.getDescription(), newDescription),
                () -> Assertions.assertEquals(task.getStatus(), newStatus),
                () -> Assertions.assertNull(task.getPlace()),
                () -> Assertions.assertEquals(task.getDeadline(), newDeadline),
                () -> Assertions.assertEquals(task.getNotificationDeadline(), newNotificationDeadline),
                ()-> Assertions.assertEquals(task.getAssignee().getId(), assigneeId)
        );
    }


    @Test
    void taskAddExpiredTest() throws Exception {

        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");

        LocalDateTime newDeadline = LocalDateTime.of(2023, 4, 20, 21, 0, 0);
        LocalDateTime newNotificationDeadline = LocalDateTime.of(2023, 4, 20, 21, 0, 0);
        /*TODO: assigner id check after adding security*/

        String taskJson = """
                {
                  "eventId": 1,
                  "assignee": {
                    "id": 1,
                    "name": "test",
                    "surname": "user"
                  },
                  "title": "CREATED",
                  "description": "created",
                  "taskStatus": "EXPIRED",
                  "place": null,
                  "deadline": "2023-04-20T21:00:00",
                  "notificationDeadline": "2023-04-20T21:00:00"
                }""";

        mockMvc.perform(post("/api/tasks")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(content().string(containsString("1")));

        Task task = taskRepository.findById(1).orElseThrow();
        Assertions.assertAll(
                () -> Assertions.assertEquals(task.getDeadline(), newDeadline),
                () -> Assertions.assertEquals(task.getNotificationDeadline(), newNotificationDeadline),
                ()-> Assertions.assertEquals(task.getStatus(), TaskStatus.EXPIRED)
        );
    }

    @Test
    void taskEditTest() throws Exception {
        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_task.sql");

        String newTitle = "UPDATED";
        String newDescription = "upd";
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;
        LocalDateTime newDeadline = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        LocalDateTime newNotificationDeadline = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        Integer assigneeId = 2;
        Integer placeId = 1;

        String taskJson = """
                {
                  "eventId": 1,
                  "assignee": {
                    "id": 2,
                    "name": "test2",
                    "surname": "user2"
                  },
                  "title": "UPDATED",
                  "description": "upd",
                  "taskStatus": "IN_PROGRESS",
                  "place": {
                    "id": 1,
                    "name": "itmo place",
                    "address": "itmo university"
                  },
                  "deadline": "2025-04-20T21:00:00",
                  "notificationDeadline": "2025-04-20T21:00:00"
                }
                """;

        mockMvc.perform(put("/api/tasks/1")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Task edited = taskRepository.findById(1).orElseThrow();
        Assertions.assertAll(
                () -> Assertions.assertEquals(edited.getTitle(), newTitle),
                () -> Assertions.assertEquals(edited.getDescription(), newDescription),
                () -> Assertions.assertEquals(edited.getStatus(), newStatus),
                () -> Assertions.assertEquals(edited.getDeadline(), newDeadline),
                () -> Assertions.assertEquals(edited.getNotificationDeadline(), newNotificationDeadline),
                ()-> Assertions.assertEquals(edited.getAssignee().getId(), assigneeId),
                ()-> {
                    Assertions.assertNotNull(edited.getPlace());
                    Assertions.assertEquals(edited.getPlace().getId(), placeId);
                }

        );
    }

    /*TODO: validation test*/

    @Test
    void taskDeleteTest() throws Exception {
        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_task.sql");

        Assertions.assertTrue(taskRepository.findById(1).isPresent());

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());

        Assertions.assertFalse(taskRepository.findById(1).isPresent());
    }

    @Test
    void taskSetAssigneeTest() throws Exception {
        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
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
                  "notificationDeadline": "2025-03-30T21:32:23.536819"
                }
                """;

        mockMvc.perform(put("/api/tasks/1/assignee/2"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedTaskJson));

        Task edited = taskRepository.findById(1).orElseThrow();

        Assertions.assertEquals(edited.getAssignee().getId(), 2);

    }


    @Test
    void taskDeleteAssigneeTest() throws Exception {
        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
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
                  "notificationDeadline": "2025-03-30T21:32:23.536819"
                }
                """;

        mockMvc.perform(delete("/api/tasks/1/assignee"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedTaskJson));

        Task task = taskRepository.findById(1).orElseThrow();

        Assertions.assertNull(task.getAssignee());

    }


    @Test
    void taskSetStatusTest() throws Exception {
        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
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
                  "notificationDeadline": "2025-03-30T21:32:23.536819"
                }
                """;

        mockMvc.perform(put("/api/tasks/1/status")
                        .content("\"IN_PROGRESS\"")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedTaskJson));

        Task task = taskRepository.findById(1).orElseThrow();

        Assertions.assertEquals(task.getStatus(), TaskStatus.IN_PROGRESS);

    }


    @Test
    void taskMoveTest() throws Exception {
        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_2.sql");
        executeSqlScript("/sql/insert_task.sql");

        Task task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(task.getEvent().getId(), 1);

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
                  "notificationDeadline": "2025-03-30T21:32:23.536819"
                }]
                """;

        mockMvc.perform(put("/api/tasks/event/2")
                        .content("[1]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedTaskJson));

        task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(task.getEvent().getId(), 2);

    }


    @Test
    void taskCopyTest() throws Exception {
        executeSqlScript("/sql/clean_tables.sql");
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_2.sql");
        executeSqlScript("/sql/insert_task.sql");

        Task task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(task.getEvent().getId(), 1);

        mockMvc.perform(post("/api/tasks/event/2")
                        .content("[1]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        task = taskRepository.findById(2).orElseThrow();
        Assertions.assertEquals(task.getEvent().getId(), 2);
        task = taskRepository.findById(1).orElseThrow();
        Assertions.assertEquals(task.getEvent().getId(), 1);

    }
}
