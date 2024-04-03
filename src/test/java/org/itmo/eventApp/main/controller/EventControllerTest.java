package org.itmo.eventApp.main.controller;

import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.repository.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Optional;
import io.minio.RemoveBucketArgs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest extends AbstractTestContainers {
    private final EventRepository eventRepository;

    @Autowired
    public EventControllerTest(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private void setUpEventData() {
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
    }

    private void setUpUserData() {
        executeSqlScript("/sql/insert_user.sql");
    }

    @AfterEach
    public void cleanUp() {
        executeSqlScript("/sql/clean_tables.sql");
    }

    @Test
    void getAllOrFilteredEventsTest() throws Exception {
        setUpEventData();
        mockMvc.perform(get("/api/events")
                        .param("title", "party")
                        .param("format", "OFFLINE")
                        .param("status", "PUBLISHED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("party"))
                .andExpect(jsonPath("$[0].format").value("OFFLINE"))
                .andExpect(jsonPath("$[0].status").value("PUBLISHED"));
    }

    @Test
    void getAllEventsTest() throws Exception {
        setUpEventData();
        mockMvc.perform(get("/api/events")
                        .param("page", "0")
                        .param("size", "15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void addProperEventByOrganizer() throws Exception {
        setUpUserData();
        String eventJson = """
                {
                    "userId": 1,
                    "title": "test event"
                }""";
        mockMvc.perform(
                        post("/api/events")
                                .content(eventJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(201));
    }

    @Test
    void addEventByOrganizerUserNotFound() throws Exception {
        String eventJson = """
                {
                    "userId": 42,
                    "title": "test event"
                }""";
        mockMvc.perform(
                        post("/api/events")
                                .content(eventJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(404))
                .andExpect(content().string(containsString("User not found")));
    }

    @Test
    void addEventByOrganizerNotNull() throws Exception {
        String eventJson = """
                {
                    "userId": 1,
                }""";
        mockMvc.perform(
                        post("/api/events")
                                .content(eventJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));

        eventJson = """
                {
                    "title": "test event"
                }""";
        mockMvc.perform(
                        post("/api/events")
                                .content(eventJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(400));
    }

    @Test
    void getEventByIdTest() throws Exception {
        setUpEventData();
        String expectedEventJson = """
                {
                  "id": 1,
                  "placeId": 1,
                  "startDate": "2024-03-30T21:32:23.536819",
                  "endDate": "2024-03-30T21:32:23.536819",
                  "title": "party",
                  "shortDescription": "cool party",
                  "fullDescription": "very cool party",
                  "format": "OFFLINE",
                  "status": "PUBLISHED",
                  "registrationStart": "2024-03-30T21:32:23.536819",
                  "registrationEnd": "2024-03-30T21:32:23.536819",
                  "parent": null,
                  "participantLimit": 10,
                  "participantAgeLowest": 5,
                  "participantAgeHighest": 7,
                  "preparingStart": "2024-03-30T21:32:23.536819",
                  "preparingEnd": "2024-03-30T21:32:23.536819"
                }""";

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedEventJson));

    }

    @Test
    void updateEventTest() throws Exception {
        setUpEventData();
        String eventRequestJson = """
                {
                  "placeId": 1,
                  "startDate": "2024-04-02T14:00:00",
                  "endDate": "2024-04-02T16:00:00",
                  "title": "New updated test title",
                  "shortDescription": "Short Description",
                  "fullDescription": "Full Description",
                  "format": "ONLINE",
                  "status": "DRAFT",
                  "registrationStart": "2024-03-16T00:00:00",
                  "registrationEnd": "2024-03-31T23:59:59",
                  "participantLimit": 30,
                  "parent": null,
                  "participantAgeLowest": 10,
                  "participantAgeHighest": 90,
                  "preparingStart": "2024-03-26T14:00:00",
                  "preparingEnd": "2024-03-31T14:00:00"
                }""";

        String expectedEventJson = """
                {
                  "id": 1,
                  "placeId": 1,
                  "startDate": "2024-04-02T14:00:00",
                  "endDate": "2024-04-02T16:00:00",
                  "title": "New updated test title",
                  "shortDescription": "Short Description",
                  "fullDescription": "Full Description",
                  "format": "ONLINE",
                  "status": "DRAFT",
                  "registrationStart": "2024-03-16T00:00:00",
                  "registrationEnd": "2024-03-31T23:59:59",
                  "participantLimit": 30,
                  "parent": null,
                  "participantAgeLowest": 10,
                  "participantAgeHighest": 90,
                  "preparingStart": "2024-03-26T14:00:00",
                  "preparingEnd": "2024-03-31T14:00:00"
                }""";

        mockMvc.perform(put("/api/events/1")
                        .content(eventRequestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedEventJson));
    }


    @Test
    void deleteEventByIdTest() throws Exception {
        setUpEventData();
        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isNoContent());

        Optional<Event> deletedEvent = eventRepository.findById(1);
        Assertions.assertFalse(deletedEvent.isPresent());
    }
}
