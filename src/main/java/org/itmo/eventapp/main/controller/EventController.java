package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.service.EventService;
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@Min(1) @PathVariable("id") Integer id,
                                         @RequestBody @Valid EventRequest eventRequest) {
        return ResponseEntity.ok().body(eventService.updateEvent(id, eventRequest));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllEvents(@Min(0) @RequestParam(value = "page", defaultValue = "0") int page,
                                          @Min(0) @Max(50) @RequestParam(value = "size", defaultValue = "15") int size) {
        return ResponseEntity.ok().body(eventService.getAllEvents(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@Min(1) @PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(eventService.getEventById(id));
    }
}
