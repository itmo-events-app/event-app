package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.controller.NotificationController;
import org.itmo.eventapp.main.model.dto.request.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class NotificationControllerTest extends AbstractTestContainers{

    @Autowired
    private NotificationController notificationController;

    private void databaseFilling(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_notifications.sql");
    }

    @Test
    @WithUserDetails("test_mail@test_mail.com")
    void getNotificationsTest() throws Exception {
        databaseFilling();
        mockMvc.perform(get("/api/notifications")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    @WithUserDetails("test_mail@test_mail.com")
    void allSeenNotificationsTest() throws Exception {
        databaseFilling();
        mockMvc.perform(put("/api/notifications")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[0].seen").value("true"))
                .andExpect(jsonPath("$[1].seen").value("true"));
    }

    @Test
    @WithUserDetails("test_mail@test_mail.com")
    void oneSeenNotificationsTest() throws Exception {
        databaseFilling();

        NotificationRequest notificationRequest = new NotificationRequest(1);
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(notificationRequest);

        mockMvc.perform(put("/api/notifications")
                        .param("page", "0")
                        .param("size", "10")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.seen").value("true"));
    }



}
