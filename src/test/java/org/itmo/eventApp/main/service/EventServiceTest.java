package org.itmo.eventApp.main.service;

import org.itmo.eventApp.main.controller.AbstractTestContainers;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;
import org.itmo.eventapp.main.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class EventServiceTest extends AbstractTestContainers {
    @Autowired
    EventService eventService;

    @BeforeEach
    public void setup() {
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
    }

    @Test
    void updateEvent() {
        EventRequest eventRequest = new EventRequest(1,
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                "Circus",
                "cool circus",
                "very cool circus",
                EventFormat.OFFLINE,
                EventStatus.PUBLISHED,
                LocalDateTime.parse("2024-03-30T21:18:23.536819"),
                LocalDateTime.parse("2024-03-30T21:18:23.536819"),
                null,
                20,
                18,
                100,
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                LocalDateTime.parse("2024-03-30T21:32:23.536819"));

        EventResponse expectedEvent = new EventResponse(1, 1,
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                "Circus",
                "cool circus",
                "very cool circus",
                EventFormat.OFFLINE,
                EventStatus.PUBLISHED,
                LocalDateTime.parse("2024-03-30T21:18:23.536819"),
                LocalDateTime.parse("2024-03-30T21:18:23.536819"),
                null,
                20,
                18,
                100,
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                LocalDateTime.parse("2024-03-30T21:32:23.536819"));

        EventResponse updatedEvent = eventService.updateEvent(1, eventRequest);
        assertAll(
                () -> assertNotNull(updatedEvent),
                () -> assertEquals(updatedEvent, expectedEvent)
        );
    }

    @Test
    void getAllOrFilteredEvents() {
        List<EventResponse> eventResponses = eventService.getAllOrFilteredEvents(0, 10, "party",
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                EventStatus.PUBLISHED, EventFormat.OFFLINE);

        EventResponse eventResponse = eventResponses.get(0);
        assertAll(
                () -> assertEquals(1, eventResponses.size()),
                () -> assertEquals("party", eventResponse.title()),
                () -> assertEquals(LocalDateTime.parse("2024-03-30T21:32:23.536819"), eventResponse.start()),
                () -> assertEquals(LocalDateTime.parse("2024-03-30T21:32:23.536819"), eventResponse.end()),
                () -> assertEquals(EventFormat.OFFLINE, eventResponse.format()),
                () -> assertEquals(EventStatus.PUBLISHED, eventResponse.status())
        );
    }

    @Test
    void getFilteredEventsForNullValues() {
        executeSqlScript("/sql/custom_data_for_event_filter.sql");
        List<EventResponse> eventResponses = eventService.getAllOrFilteredEvents(0, 10, null,
                null, null, null, null);

        assertEquals(6, eventResponses.size());
    }

    @Test
    void getPartialFilteringEventsByEnums() {
        executeSqlScript("/sql/custom_data_for_event_filter.sql");

        List<EventResponse> eventResponses = eventService.getAllOrFilteredEvents(0, 10, null,
                null, null, EventStatus.PUBLISHED, EventFormat.ONLINE);

        assertEquals(2, eventResponses.size());
        eventResponses.forEach(eventResponse -> assertAll(
                () -> assertEquals(EventStatus.PUBLISHED, eventResponse.status()),
                () -> assertEquals(EventFormat.ONLINE, eventResponse.format()))
        );
    }

    @Test
    void getPartialFilteringEventsByDateAndTitle() {
        executeSqlScript("/sql/custom_data_for_event_filter.sql");

        List<EventResponse> eventResponses = eventService.getAllOrFilteredEvents(0, 10, "m",
                LocalDateTime.parse("2024-04-02T05:07:00.000000"), null, null, null);

        assertEquals(1, eventResponses.size());
        eventResponses.forEach(eventResponse -> assertAll(
                () -> assertEquals(eventResponse.title(), "m"),
                () -> assertEquals(eventResponse.start(), LocalDateTime.parse("2024-04-02T05:07:00.000000")))
        );
    }

    @Test
    void getEventById() {
        EventResponse expectedEvent = new EventResponse(1, 1,
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                "party",
                "cool party",
                "very cool party",
                EventFormat.OFFLINE,
                EventStatus.PUBLISHED,
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                null,
                10,
                5,
                7,
                LocalDateTime.parse("2024-03-30T21:32:23.536819"),
                LocalDateTime.parse("2024-03-30T21:32:23.536819"));

        EventResponse actualEvent = eventService.getEventById(1);

        assertAll(
                () -> assertNotNull(actualEvent),
                () -> assertEquals(expectedEvent, actualEvent)
        );
    }
}