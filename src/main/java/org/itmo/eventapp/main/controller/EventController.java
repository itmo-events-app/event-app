package org.itmo.eventapp.main.controller;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EventController {
    private EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<?> addEvent(@RequestBody EventRequest eventRequest) {
        return eventService.addEvent(eventRequest);
    }
}
