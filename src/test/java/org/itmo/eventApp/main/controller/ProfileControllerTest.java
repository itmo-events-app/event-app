package org.itmo.eventApp.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.itmo.eventapp.main.model.dto.request.NotificationSettingsRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangeLoginRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangeNameRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangePasswordRequest;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileControllerTest extends AbstractTestContainers {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private UserLoginInfo getUserLoginInfo() {
        UserLoginInfo userDetails = new UserLoginInfo();
        User dummyUser = new User();
        dummyUser.setId(1);
        userDetails.setUser(dummyUser);
        return userDetails;
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void testChangeName() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        UserChangeNameRequest request = new UserChangeNameRequest("Кодзима", "Гений");
        mockMvc.perform(put("/api/profile/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());


        User updatedUser = userRepository.findById(1).orElse(null);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("Кодзима", updatedUser.getName());
        Assertions.assertEquals("Гений", updatedUser.getSurname());
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void testChangePassword() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        UserChangePasswordRequest request = new UserChangePasswordRequest("old123passwordNEW!", "123passwordNEW!", "123passwordNEW!");
        mockMvc.perform(put("/api/profile/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void testChangeMismatchPassword() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "123passwordNEW123!", "123passwordNEW321!");
        mockMvc.perform(put("/api/profile/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void testChangeEmail() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        UserChangeLoginRequest request = new UserChangeLoginRequest("newEmail@itmo.ru", LoginType.EMAIL);
        mockMvc.perform(put("/api/profile/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        User updatedUser = userRepository.findById(1).orElse(null);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("newEmail@itmo.ru", updatedUser.getUserLoginInfo().getLogin());
    }


    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void testChangeToExistEmail() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_user_3.sql");

        UserChangeLoginRequest request = new UserChangeLoginRequest("test_mail3@itmo.ru", LoginType.EMAIL);
        mockMvc.perform(put("/api/profile/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void testGetUserInfo() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        mockMvc.perform(get("/api/profile/me"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userInfo[0].login").value("test_mail@itmo.ru"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userInfo[0].type").value("EMAIL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastLoginDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.enablePushNotifications").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enableEmailNotifications").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.devices").isArray());
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    void testUpdateNotifications() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        NotificationSettingsRequest request = new NotificationSettingsRequest(false, true);
        mockMvc.perform(put("/api/profile/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        User updatedUser = userRepository.findById(1).orElse(null);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertFalse(updatedUser.getUserNotificationInfo().isEnableEmailNotifications());
        Assertions.assertTrue(updatedUser.getUserNotificationInfo().isEnablePushNotifications());
    }

    @Test
    @WithMockUser(username = "test_mail@test_mail.com")
    void testGetUserEventPrivilegesNotFound() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        // Do not include insert_event_role here, because request will fail with 403
//        executeSqlScript("/sql/insert_event_role.sql");

        mockMvc.perform(get("/api/profile/event-privileges/1")
                        .with(user(getUserLoginInfo())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserEventPrivileges() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        UserLoginInfo userLoginInfo = getUserLoginInfo();
        String eventJson = """
                {
                    "userId": 1,
                    "title": "test event"
                }""";
        mockMvc.perform(
                post("/api/events")
                        .content(eventJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(userLoginInfo))
        );

        MvcResult result = mockMvc.perform(get("/api/profile/event-privileges/1")
                        .with(user(userLoginInfo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andReturn();
        String resultString = result.getResponse().getContentAsString();
        Assertions.assertTrue(resultString.contains("ASSIGN_TASK_EXECUTOR"));
        Assertions.assertTrue(resultString.contains("DELETE_EVENT_ACTIVITIES"));
        Assertions.assertTrue(resultString.contains("IMPORT_PARTICIPANT_LIST_XLSX"));
        Assertions.assertTrue(resultString.contains("ASSIGN_ORGANIZER_ROLE"));
    }

    @Test
    @WithMockUser(username = "test_mail@test_mail.com")
    void testGetBaseInfo() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        MvcResult result = mockMvc.perform(get("/api/profile/system-privileges")
                        .with(user(getUserLoginInfo())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
        String resultString = result.getResponse().getContentAsString();
        Assertions.assertTrue(resultString.contains("CREATE_EVENT_VENUE"));
        Assertions.assertTrue(resultString.contains("VIEW_EVENT_ACTIVITIES"));
        Assertions.assertTrue(resultString.contains("MODIFY_PROFILE_DATA"));
        Assertions.assertTrue(resultString.contains("VIEW_ALL_EVENTS"));
    }
}
