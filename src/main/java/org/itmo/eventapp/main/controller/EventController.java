package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events")
@Validated
public class EventController {
    private final EventService eventService;
    @PostMapping(value = "/activity", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> addActivity(@Valid EventRequest eventRequest) {
        return ResponseEntity.ok(eventService.addEvent(eventRequest).getId());
    }

    // TODO: Return images in response
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@Min(1) @PathVariable("id") Integer id,
                                                     @Valid EventRequest eventRequest) {
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
        return ResponseEntity.ok().body(eventService.getEventById(id));
    }
}
