package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.mapper.EventMapper;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.repository.PlaceRepository;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;

    private static final String EVENT_NOT_FOUND_MESSAGE = "Event not found";
    private static final String EVENT_PARENT_NOT_FOUND_MESSAGE = "Event's parent not found";
    private static final String PLACE_NOT_FOUND_MESSAGE = "Place not found";

    public Event addEvent(EventRequest eventRequest) {
        // TODO: Add privilege validation
        Place place = placeRepository.findById(eventRequest.placeId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLACE_NOT_FOUND_MESSAGE));

        Event parent = null;
        if (eventRequest.parent() != null) {
            parent = eventRepository.findById(eventRequest.parent()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_MESSAGE));
        }
        Event e = Event.builder()
                .place(place)
                .startDate(eventRequest.start())
                .endDate(eventRequest.end())
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

    public Event findById(int id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_MESSAGE);
        }

        return event.get();
    }

    public EventResponse updateEvent(Integer id, EventRequest eventRequest) {
        Event parentEvent = null;
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_MESSAGE);
        }
        Place place = placeRepository.findById(eventRequest.placeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PLACE_NOT_FOUND_MESSAGE));
        if (eventRequest.parent() != null) {
            parentEvent = eventRepository.findById(eventRequest.parent())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_PARENT_NOT_FOUND_MESSAGE));
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

    public EventResponse getEventById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_MESSAGE));
        return EventMapper.eventToEventResponse(event);
    }
}
