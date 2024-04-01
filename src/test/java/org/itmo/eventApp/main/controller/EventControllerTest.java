package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest extends AbstractTestContainers{
    @Test
    void addProperEvent() throws Exception {
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        String eventJson = "{" +
                "\"placeId\": 1," +
                "\"start\": \"2024-03-28T09:00:00\"," +
                "\"end\": \"2024-03-28T18:00:00\"," +
                "\"title\": \"as\"," +
                "\"shortDescription\": \"This is a short description.\"," +
                "\"fullDescription\": \"This is a full description of the event.\"," +
                "\"format\": \"OFFLINE\"," +
                "\"status\": \"PUBLISHED\"," +
                "\"registrationStart\": \"2024-03-01T00:00:00\"," +
                "\"registrationEnd\": \"2024-03-25T23:59:59\"," +
                "\"parent\": 1," +
                "\"participantLimit\": 50," +
                "\"participantAgeLowest\": 18," +
                "\"participantAgeHighest\": 50," +
                "\"preparingStart\": \"2024-03-20T00:00:00\"," +
                "\"preparingEnd\": \"2024-03-27T23:59:59\"" +
                "}";
        mockMvc.perform(
                        post("/api/events/activity")
                                .content(eventJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void addPlaceNotFoundInvalidEvent() throws Exception {
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        String eventJson = "{" +
                "\"placeId\": 10000," +
                "\"start\": \"2024-03-28T09:00:00\"," +
                "\"end\": \"2024-03-28T18:00:00\"," +
                "\"title\": \"as\"," +
                "\"shortDescription\": \"This is a short description.\"," +
                "\"fullDescription\": \"This is a full description of the event.\"," +
                "\"format\": \"OFFLINE\"," +
                "\"status\": \"PUBLISHED\"," +
                "\"registrationStart\": \"2024-03-01T00:00:00\"," +
                "\"registrationEnd\": \"2024-03-25T23:59:59\"," +
                "\"parent\": 1," +
                "\"participantLimit\": 50," +
                "\"participantAgeLowest\": 18," +
                "\"participantAgeHighest\": 50," +
                "\"preparingStart\": \"2024-03-20T00:00:00\"," +
                "\"preparingEnd\": \"2024-03-27T23:59:59\"" +
                "}";
        mockMvc.perform(
                        post("/api/events/activity")
                                .content(eventJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(404))
                .andExpect(content().string(containsString("Place not found")));;
    }

    @Test
    void addEmptyTitleInvalidEvent() throws Exception {
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        String eventJson = "{" +
                "\"placeId\": 1," +
                "\"start\": \"2024-03-28T09:00:00\"," +
                "\"end\": \"2024-03-28T18:00:00\"," +
                "\"title\": \"\"," +
                "\"shortDescription\": \"This is a short descirption\"," +
                "\"fullDescription\": \"This is a full description of the event.\"," +
                "\"format\": \"OFFLINE\"," +
                "\"status\": \"PUBLISHED\"," +
                "\"registrationStart\": \"2024-03-01T00:00:00\"," +
                "\"registrationEnd\": \"2024-03-25T23:59:59\"," +
                "\"parent\": 1," +
                "\"participantLimit\": 50," +
                "\"participantAgeLowest\": 18," +
                "\"participantAgeHighest\": 50," +
                "\"preparingStart\": \"2024-03-20T00:00:00\"," +
                "\"preparingEnd\": \"2024-03-27T23:59:59\"" +
                "}";
        mockMvc.perform(
                        post("/api/events/activity")
                                .content(eventJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));
    }
}
