package org.itmo.eventApp.main.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AbstractTestContainers {

    @Test
    public void methodUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/hello").param("s", "world")).andExpect(status().isUnauthorized());
    }

    @Test
    public void methodAuthorizedTest() throws Exception {
        mockMvc.perform(get("/hello").param("s", "world").with(jwt())).andExpect(status().isOk());
    }
}
