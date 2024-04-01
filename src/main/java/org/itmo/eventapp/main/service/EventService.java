package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.mapper.EventMapper;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.repository.PlaceRepository;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;


    public Event addEvent(EventRequest eventRequest) {
        // TODO: Add privilege validation
        Place place = placeRepository.findById(eventRequest.placeId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));

        Event parent = null;
        if (eventRequest.parent() != null) {
            parent = eventRepository.findById(eventRequest.parent()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        }
        Event e = EventMapper.eventRequestToEvent(eventRequest, place, parent);
        return eventRepository.save(e);
    }

    public Event findById(int id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }

        return event.get();
    }

    // TODO: Add filters
    public List<EventResponse> listEvents() {
        List<Event> events = eventRepository.findAll();
        List<EventResponse> eventRequests = new ArrayList<>();
        for (Event event : events) {
            eventRequests.add(EventMapper.eventToEventResponse(event));
        }
        return eventRequests;
    }

}
