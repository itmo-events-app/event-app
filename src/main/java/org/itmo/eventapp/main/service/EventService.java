package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.repository.PlaceRepository;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {

    private EventRepository eventRepository;
    private PlaceRepository placeRepository;

    @Autowired
    public EventService(EventRepository eventRepo, PlaceRepository placeRepo) {
        this.eventRepository = eventRepo;
        this.placeRepository = placeRepo;
    }
    public ResponseEntity<Integer> addEvent(EventRequest eventRequest) {
        // TODO: Add privilege validation
        Optional<Place> place = placeRepository.findById(eventRequest.placeId());

        if (place.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        Event event = null;
        if (eventRequest.parent() != null) {
            Optional<Event> eventOptional = eventRepository.findById(eventRequest.parent());
            if (eventOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
            }
            event = eventOptional.get();
        }
        Event e = Event.builder()
                .place(place.get()) // TODO set place
                .startDate(eventRequest.start())
                .endDate(eventRequest.end())
                .title(eventRequest.title())
                .shortDescription(eventRequest.shortDescription())
                .fullDescription(eventRequest.fullDescription())
                .format(eventRequest.format())
                .status(eventRequest.status())
                .registrationStart(eventRequest.registrationStart())
                .registrationEnd(eventRequest.registrationEnd())
                .parent(event) // TODO set parent
                .participantLimit(eventRequest.participantLimit())
                .participantAgeLowest(eventRequest.participantAgeLowest())
                .participantAgeHighest(eventRequest.participantAgeHighest())
                .preparingStart(eventRequest.preparingStart())
                .preparingEnd(eventRequest.preparingEnd())
                .build();
        eventRepository.save(e);
        return ResponseEntity.ok().body(e.getId());
    }

    public Event findById(int id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }

        return event.get();
    }
}
