package org.itmo.eventApp.main.controller;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestControllerTest extends AbstractTestContainers {
    @Test
    public void sayHelloTest() throws Exception {
        mockMvc.perform(get("/hello").param("s", "test"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, test!")));
    }
}
