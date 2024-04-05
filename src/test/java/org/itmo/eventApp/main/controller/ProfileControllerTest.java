package org.itmo.eventApp.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.itmo.eventapp.main.model.dto.request.UserChangeEmailRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangeNameRequest;
import org.itmo.eventapp.main.model.dto.request.UserChangePasswordRequest;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class ProfileControllerTest extends AbstractTestContainers {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    public void testChangeName() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        UserChangeNameRequest request = new UserChangeNameRequest("Кодзима", "Гений");
        mockMvc.perform(put("/api/profile/change-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());


        User updatedUser = userRepository.findById(1).orElse(null);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("Кодзима", updatedUser.getName());
        Assertions.assertEquals("Гений", updatedUser.getSurname());
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    public void testChangePassword() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "newPassword", "newPassword");
        mockMvc.perform(put("/api/profile/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    public void testChangeMismatchPassword() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "test123123", "qwerty123");
        mockMvc.perform(put("/api/profile/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    public void testChangeEmail() throws Exception {
        executeSqlScript("/sql/insert_user.sql");

        UserChangeEmailRequest request = new UserChangeEmailRequest("newEmail@itmo.ru");
        mockMvc.perform(put("/api/profile/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        User updatedUser = userRepository.findById(1).orElse(null);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("newEmail@itmo.ru", updatedUser.getUserLoginInfo().getEmail());
    }


    @Test
    @WithMockUser(username = "test_mail@itmo.ru")
    public void testChangeToExistEmail() throws Exception {
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_user_3.sql");

        UserChangeEmailRequest request = new UserChangeEmailRequest("test_mail3@itmo.ru");
        mockMvc.perform(put("/api/profile/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}
