package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.itmo.eventapp.main.model.dto.request.CreateEventRequest;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.repository.PlaceRepository;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.repository.EventRoleRepository;
import org.itmo.eventapp.main.repository.RoleRepository;
import org.itmo.eventapp.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

        Event parent = eventRepository.findById(eventRequest.parent())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent event not found"));
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

    public Event addEventByOrganizer(CreateEventRequest eventRequest) {
        // TODO: Add privilege validation
        Event e = Event.builder()
                .title(eventRequest.title())
                .build();
        Event savedEvent = eventRepository.save(e);

        User user = userService.findById(eventRequest.userId());

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
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }

        return event.get();
    }
}
