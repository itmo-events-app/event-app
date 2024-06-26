package org.itmo.eventApp.main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.itmo.eventapp.main.minio.MinioService;
import org.itmo.eventapp.main.model.dto.request.EditEventRequest;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.model.dto.response.PaginatedResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.entity.PlaceRow;
import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;
import org.itmo.eventapp.main.model.mapper.EventMapper;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.repository.PlaceRepository;
import org.itmo.eventapp.main.service.EventRoleService;
import org.itmo.eventapp.main.service.EventService;
import org.itmo.eventapp.main.service.PlaceService;
import org.itmo.eventapp.main.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private PlaceService placeService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Event> query;
    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Event> root;

    @Mock
    private MinioService minioService;
    @Mock
    private TaskService taskService;
    @Mock
    private EventRoleService eventRoleService;
    @Mock
    private TypedQuery<Event> typedQuery;
    @Mock
    private TypedQuery<Long> countTypedQuery;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        eventService.setEntityManager(entityManager);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    }

    /*@Test
    void testUpdateEvent() {
        Integer eventId = 1;
        EditEventRequest eventRequest = new EditEventRequest(
            1,
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

            LocalDateTime.parse("2024-03-30T21:32:23.536819"),
            null
        );
        Place place = new Place();
        List<PlaceRow> places = new ArrayList<>();
        when(placeRepository.findById(any())).thenReturn(Optional.of(place));
        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.save(any())).thenReturn(new Event());
        when(placeService.findById(anyInt())).thenReturn(new Place());
        doNothing().when(minioService).deleteImageByPrefix(anyString(), any());
        Event updatedEvent = eventService.updateEvent(eventId, eventRequest);

        assertAll(
            () -> assertNotNull(updatedEvent),
            () -> assertEquals(EventMapper.editEventRequestToEvent(1, eventRequest, places, null), updatedEvent)
        );
    }*/

    @Test
    void testGetAllOrFilteredEvents() {
        String title = "new test event party";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        EventStatus status = EventStatus.PUBLISHED;
        EventFormat format = EventFormat.OFFLINE;
        int page = 0;
        int size = 10;
        List<Event> expectedEvents = Arrays.asList(new Event(), new Event());

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Event.class)).thenReturn(query);
        when(query.from(Event.class)).thenReturn(root);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedEvents);

        PaginatedResponse<Event> result = eventService.getAllOrFilteredEvents(page, size, null, title, startDate, endDate, status, format);

        verify(criteriaBuilder).like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        verify(criteriaBuilder).greaterThanOrEqualTo(root.get("startDate"), startDate);
        verify(criteriaBuilder).lessThanOrEqualTo(root.get("endDate"), endDate);
        verify(criteriaBuilder).equal(root.get("status"), status);
        verify(criteriaBuilder).equal(root.get("format"), format);

        assertEquals(2, result.total());
        assertEquals(expectedEvents, result.items());
    }

    @Test
    void testGetEventById() {
        Integer eventId = 1;
        Event expectedEvent = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(expectedEvent));
        Event actualEvent = eventService.getEventById(eventId);
        assertEquals(expectedEvent, actualEvent);
    }


    @Test
    void testGetEventByIdNotFound() {
        Integer eventId = 1;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> eventService.getEventById(eventId));
    }


    @Test
    void testDeleteEventById() {
        Integer eventId = 1;
        assertThrows(ResponseStatusException.class, () -> eventService.deleteActivityById(eventId));
        verify(eventRepository, times(0)).deleteById(eventId);
    }

    @Test
    void testCopyEventNoChilds() {
        Event existingEvent = Event.builder()
            .id(1).title("Existing event")
            .startDate(LocalDateTime.of(2024, 4, 1, 10, 0))
            .endDate(LocalDateTime.of(2024, 4, 2, 10, 0))
            .build();
        when(eventRepository.findById(1)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event event = invocation.getArgument(0);
            event.setId(2);
            return event;
        });
        Event copiedEvent = eventService.copyEvent(1, false);

        verify(eventRepository).save(any(Event.class));
        assertEquals(2, copiedEvent.getId());
        assertEquals(existingEvent.getStartDate(), copiedEvent.getStartDate());
        assertEquals(existingEvent.getEndDate(), copiedEvent.getEndDate());
        assertEquals(existingEvent.getTitle(), copiedEvent.getTitle());
    }

    @Test
    void testCopyEvent() {
        int[] startingId = new int[]{1};
        Event existingEvent = Event.builder()
            .id(startingId[0]++)
            .build();
        Event childEvent = Event.builder()
            .id(startingId[0]++)
            .build();
        List<Event> childEvents = new ArrayList<>();
        childEvents.add(childEvent);
        when(eventRepository.findById(1)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.findAllByParent_Id(1)).thenReturn(childEvents);
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event event = invocation.getArgument(0);
            event.setId(startingId[0]++);
            return event;
        });
        Event copiedEvent = eventService.copyEvent(1, true);

        verify(eventRepository, times(2)).save(any(Event.class));
        assertEquals(5, startingId[0]);
        assertNotEquals(existingEvent.getId(), copiedEvent.getId());
    }

    @Test
    void testCopyEventNotFound() {
        when(eventRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> eventService.copyEvent(1, true));
        verify(eventRepository, never()).save(any(Event.class));
    }
}