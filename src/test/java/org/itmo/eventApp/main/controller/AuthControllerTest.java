package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.security.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AbstractTestContainers {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    @WithAnonymousUser
    public void methodUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/hello").param("s", "world")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "login@gmail.com")
    public void methodAuthorizedTest() throws Exception {
        mockMvc.perform(get("/hello").param("s", "world"))
                .andExpect(status().isOk());
    }
}
