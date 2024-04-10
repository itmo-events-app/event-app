package org.itmo.eventApp.main.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends AbstractTestContainers {


    //todo assert incorrect, need to fix
//    @Test
//    @WithAnonymousUser
//    void methodUnauthorizedTest() throws Exception {
//        mockMvc.perform(get("/hello").param("s", "world")).andExpect(status().isUnauthorized());
//    }

    @Test
    @WithMockUser(username = "login@gmail.com")
    void methodAuthorizedTest() throws Exception {
        mockMvc.perform(get("/hello").param("s", "world"))
                .andExpect(status().isOk());
    }
}
