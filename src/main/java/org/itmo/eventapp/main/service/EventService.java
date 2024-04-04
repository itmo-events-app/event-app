package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.CreateEventRequest;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.mapper.EventMapper;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    private final PlaceService placeService;
    private final UserService userService;
    private final RoleService roleService;
    private final EventRoleService eventRoleService;

    public Event addEvent(EventRequest eventRequest) {
        // TODO: Add privilege validation
        Place place = placeService.findById(eventRequest.placeId());

        Event parent = findById(eventRequest.parent());
        Event e = Event.builder()
                .place(place)
                .startDate(eventRequest.startDate())
                .endDate(eventRequest.endDate())
                .title(eventRequest.title())
                .shortDescription(eventRequest.shortDescription())
                .fullDescription(eventRequest.fullDescription())
                .format(eventRequest.format())
                .status(eventRequest.status())
                .registrationStart(eventRequest.registrationStart())
                .registrationEnd(eventRequest.registrationEnd())
                .parent(parent)
                .participantLimit(eventRequest.participantLimit())
                .participantAgeLowest(eventRequest.participantAgeLowest())
                .participantAgeHighest(eventRequest.participantAgeHighest())
                .preparingStart(eventRequest.preparingStart())
                .preparingEnd(eventRequest.preparingEnd())
                .build();
        return eventRepository.save(e);
    }

    public Event addEventByOrganizer(CreateEventRequest eventRequest) {
        Event e = Event.builder()
                .title(eventRequest.title())
                .build();
        Event savedEvent = eventRepository.save(e);

        User user = userService.findById(eventRequest.userId());

        // TODO: Do not get organizer from DB each time.
        Role role = roleService.findByName("Организатор");

        EventRole eventRole = EventRole.builder()
                .user(user)
                .role(role)
                .event(savedEvent)
                .build();
        eventRoleService.save(eventRole);
        return savedEvent;
    }

    public Event findById(int id) {
        return eventRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE));
    }

    public EventResponse updateEvent(Integer id, EventRequest eventRequest) {
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE);
        }
        Place place = placeService.findById(eventRequest.placeId());
        Event parentEvent = null;
        if (eventRequest.parent() != null) {
            parentEvent = eventRepository.findById(eventRequest.parent())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_PARENT_NOT_FOUND_MESSAGE));
        }
        Event updatedEvent = EventMapper.eventRequestToEvent(id, eventRequest, place, parentEvent);
        eventRepository.save(updatedEvent);
        return EventMapper.eventToEventResponse(updatedEvent);
    }

    public List<EventResponse> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.findAll(pageable);
        List<Event> events = eventPage.getContent();
        return EventMapper.eventsToEventResponseList(events);
    }

    public EventResponse getEventResponseById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE));
        return EventMapper.eventToEventResponse(event);
    }
}
