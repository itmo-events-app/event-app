package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.CreateEventRequest;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events")
@Validated
public class EventController {
    private final EventService eventService;

    // TODO: Add privilege validation

    @PostMapping("/activity")
    public ResponseEntity<Integer> addEvent(@RequestBody @Valid EventRequest eventRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.addEvent(eventRequest).getId());
    }

    @PostMapping
    public ResponseEntity<Integer> addEventByOrganizer(@RequestBody @Valid CreateEventRequest eventRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.addEventByOrganizer(eventRequest).getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@Min(1) @PathVariable("id") Integer id,
                                                     @RequestBody @Valid EventRequest eventRequest) {
        return ResponseEntity.ok().body(eventService.updateEvent(id, eventRequest));
    }

    // TODO: Add filtering by fields: title, dates, status, format

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents(@Min(0) @RequestParam(value = "page", defaultValue = "0") int page,
                                                            @Min(0) @Max(50) @RequestParam(value = "size", defaultValue = "15") int size) {
        return ResponseEntity.ok().body(eventService.getAllEvents(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@Min(1) @PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(eventService.getEventResponseById(id));
    }
}
