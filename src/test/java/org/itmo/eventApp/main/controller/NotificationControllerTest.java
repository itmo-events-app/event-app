package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.model.entity.Notification;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationControllerTest extends AbstractTestContainers{
    @Autowired
    private NotificationRepository notificationRepository;

    private void databaseFilling(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_notifications.sql");
    }

    private UserLoginInfo getUserLoginInfo(){
        UserLoginInfo userDetails = new UserLoginInfo();
        User dummyUser = new User();
        dummyUser.setId(1);
        userDetails.setUser(dummyUser);
        return userDetails;
    }


    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void getNotificationsTest() throws Exception {
        databaseFilling();
        mockMvc.perform(get("/api/notifications")
                        .param("page", "0")
                        .param("size", "10")
                        .with(user(getUserLoginInfo())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void allSeenNotificationsTest() throws Exception {
        databaseFilling();
        mockMvc.perform(put("/api/notifications")
                        .param("page", "0")
                        .param("size", "10")
                        .with(user(getUserLoginInfo())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[0].seen").value("true"))
                .andExpect(jsonPath("$[1].seen").value("true"));
        ArrayList<Notification> notifications = (ArrayList<Notification>) notificationRepository.getAllByUserId(1, null);
        for (Notification n : notifications) {
            assertTrue(n.isSeen());
        }
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void oneSeenNotificationsTest() throws Exception {
        databaseFilling();
        mockMvc.perform(put("/api/notifications/1")
                        .param("page", "0")
                        .param("size", "10")
                        .with(user(getUserLoginInfo())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.seen").value("true"));
        Notification notification = notificationRepository.findById(1).get();
        assertTrue(notification.isSeen());
    }
}
