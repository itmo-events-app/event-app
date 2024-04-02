package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

//    @Test
//    void taskAddTest() throws Exception {
//        mockMvc.perform(post("/api/tasks")
//                        .content(loadAsString("src/test/resources/json/taskAdd.json"))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(201));
//        // TODO        .andExpect(content().string(containsString("1")));
//    }
}
