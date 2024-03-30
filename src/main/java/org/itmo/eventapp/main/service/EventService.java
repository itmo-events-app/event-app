package org.itmo.eventapp.main.service;

import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.repository.EventRepo;
import org.itmo.eventapp.main.repository.PlaceRepo;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private PlaceRepo placeRepo;
    public ResponseEntity<?> addEvent(EventRequest eventRequest){
        // TODO: Add privilege validation
        Optional<Place> place = placeRepo.findById(eventRequest.placeId());
        if(place.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found");
        Event e = new Event();
        e.setStart(eventRequest.start());
        e.setEnd(eventRequest.end());
        e.setTitle(eventRequest.title());
        e.setParent(null);
        e.setFormat(eventRequest.format());
        e.setParticipantAgeHighest(eventRequest.participantsAgeHighest());
        e.setFullDescription(eventRequest.fullDescription());
        e.setParticipantAgeLowest(eventRequest.participantsAgeLowest());
        e.setPlace(place.get());
        e.setStatus(eventRequest.status());
        e.setRegistrationStart(eventRequest.registrationStart());
        e.setRegistrationEnd(eventRequest.registrationEnd());
        e.setShortDescription(eventRequest.shortDescription());
        e.setParticipantLimit(eventRequest.participantsLimit());
        e.setPreparingStart(eventRequest.preparingStart());
        e.setPreparingEnd(eventRequest.preparingEnd());
        eventRepo.save(e);
        return ResponseEntity.ok().body(e.getId());
    }
}
