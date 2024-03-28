package org.itmo.eventapp.main.service;

import org.itmo.eventapp.main.model.Event;
import org.itmo.eventapp.main.model.Place;
import org.itmo.eventapp.main.repo.EventRepo;
import org.itmo.eventapp.main.repo.PlaceRepo;
import org.itmo.eventapp.main.request.EventRequest;
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
        Optional<Place> place = placeRepo.findById(eventRequest.getPlaceId());
        if(place.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found");
        Event e = new Event();
        e.setStart(eventRequest.getStart());
        e.setEnd(eventRequest.getEnd());
        e.setTitle(eventRequest.getTitle());
        e.setParent(null);
        e.setFormat(eventRequest.getFormat());
        e.setParticipantsAgeHighest(eventRequest.getParticipantsAgeHighest());
        e.setFullDescription(eventRequest.getFullDescription());
        e.setParticipantsAgeLowest(eventRequest.getParticipantsAgeLowest());
        e.setPlace(place.get());
        e.setStatus(eventRequest.getStatus());
        e.setRegistrationStart(eventRequest.getRegistrationStart());
        e.setRegistrationEnd(eventRequest.getRegistrationEnd());
        e.setShortDescription(eventRequest.getShortDescription());
        e.setParticipantLimit(eventRequest.getParticipantsLimit());
        e.setPreparingStart(eventRequest.getPreparingStart());
        e.setPreparingEnd(eventRequest.getPreparingEnd());
        eventRepo.save(e);
        return ResponseEntity.ok().build();
    }
}
