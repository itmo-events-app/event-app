package org.itmo.eventapp.main.controller;

import org.itmo.eventapp.main.model.dto.EventRequest;
import org.itmo.eventapp.main.service.EventService;
import org.itmo.eventapp.main.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    @Autowired
    private EventService eventService;
    @PostMapping("/events")
    public ResponseEntity<?> addEvent(@RequestBody EventRequest eventRequest){
        return eventService.addEvent(eventRequest);
    }
}
