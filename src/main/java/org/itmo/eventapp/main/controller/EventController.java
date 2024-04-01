package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events")
@Validated
public class EventController {
    private final EventService eventService;
    @PostMapping
    public ResponseEntity<Integer> addEvent(@RequestBody @Valid EventRequest eventRequest) {
        return ResponseEntity.ok(eventService.addEvent(eventRequest).getId());
    }
}
