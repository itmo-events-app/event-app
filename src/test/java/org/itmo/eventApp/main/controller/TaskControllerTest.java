package org.itmo.eventApp.main.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.itmo.eventapp.main.model.dto.request.PlaceShortDataRequest;
import org.itmo.eventapp.main.model.dto.request.TaskRequest;
import org.itmo.eventapp.main.model.dto.request.UserShortDataRequest;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest extends AbstractTestContainers {
    @Autowired
    TaskRepository taskRepository;

    @Test
    void taskGetTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_task.sql");

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "{\"title\":\"VERY DIFFICULT TASK\",\"description\":\"write sql script for tests\"," +
                                "\"taskStatus\":\"NEW\",\"deadline\":\"2024-03-30T21:32:23.536819\"," +
                                "\"notificationDeadline\":\"2024-03-30T21:32:23.536819\"}")));
    }

    @Test
    void taskGetInvalidIdTest() throws Exception {
        mockMvc.perform(get("/api/tasks/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("taskGet.id: Параметр id не может быть меньше 1!")));
    }

    @Test
    void taskAddTest() throws Exception {

        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
//        executeSqlScript("/sql/insert_task.sql");

        String newTitle = "CREATED";
        String newDescription = "created";
        TaskStatus newStatus = TaskStatus.NEW;
        LocalDateTime newDeadline = LocalDateTime.of(2024, 4, 20, 21, 0, 0);
        LocalDateTime newNotificationDeadline = LocalDateTime.of(2024, 4, 20, 21, 0, 0);

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
                  "deadline": "2024-04-20T21:00:00",
                  "notificationDeadline": "2024-04-20T21:00:00"
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
                () -> Assertions.assertEquals(task.getNotificationDeadline(), newNotificationDeadline)
        );
    }

    @Test
    void taskEditTest() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_task.sql");

//        UserShortDataRequest assignee = new UserShortDataRequest(
//                1,
//                "test",
//                "user"
//        );
//
        String newTitle = "UPDATED";
        String newDescription = "upd";
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;
        LocalDateTime newDeadline = LocalDateTime.of(2024, 4, 20, 21, 0, 0);
        LocalDateTime newNotificationDeadline = LocalDateTime.of(2024, 4, 20, 21, 0, 0);
//
//        TaskRequest taskRequest = new TaskRequest(
//                1,
//                assignee,
//                newTitle,
//                newDescription,
//                newStatus,
//                null,
//                newDeadline,
//                newNotificationDeadline
//        );

//        Integer eventId,
//        @NotNull(message = "Поле assignee не может быть null!")
//        UserShortDataRequest assignee,
//        @NotBlank(message = "Поле title не может быть пустым!")
//        String title,
//        @NotNull(message = "Поле description не может быть null!")
//        String description,
//        TaskStatus taskStatus,
//        PlaceShortDataRequest place,
//        @NotNull(message = "Поле deadline не может быть null!")
//        LocalDateTime deadline,
//        @NotNull(message = "Поле notificationDeadline не может быть null!")
//        LocalDateTime notificationDeadline

        String taskJson = """
                {
                  "eventId": 1,
                  "assignee": {
                    "id": 1,
                    "name": "test",
                    "surname": "user"
                  },
                  "title": "UPDATED",
                  "description": "upd",
                  "taskStatus": "IN_PROGRESS",
                  "place": null,
                  "deadline": "2024-04-20T21:00:00",
                  "notificationDeadline": "2024-04-20T21:00:00"
                }""";

//        ObjectMapper mapper = new ObjectMapper();
//        String jsonContent = mapper.writeValueAsString(taskRequest);

        mockMvc.perform(put("/api/tasks/1")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Task edited = taskRepository.findById(1).orElseThrow();
        Assertions.assertAll(
                () -> Assertions.assertEquals(edited.getTitle(), newTitle),
                () -> Assertions.assertEquals(edited.getDescription(), newDescription),
                () -> Assertions.assertEquals(edited.getStatus(), newStatus),
                () -> Assertions.assertNull(edited.getPlace()),
                () -> Assertions.assertEquals(edited.getDeadline(), newDeadline),
                () -> Assertions.assertEquals(edited.getNotificationDeadline(), newNotificationDeadline)
        );
    }
}
