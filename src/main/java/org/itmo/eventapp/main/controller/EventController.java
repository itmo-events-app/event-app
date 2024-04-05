package org.itmo.eventapp.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.CreateEventRequest;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;
import org.itmo.eventapp.main.model.mapper.EventMapper;
import org.itmo.eventapp.main.model.dto.response.UserRoleResponse;
import org.itmo.eventapp.main.model.mapper.EventRoleMapper;
import org.itmo.eventapp.main.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events")
@Validated
public class EventController {
    private final EventService eventService;
    // TODO: Add privilege validation
    @PostMapping(value = "/activity", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> addActivity(@Valid EventRequest eventRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.addEvent(eventRequest).getId());
    }

    @PostMapping
    public ResponseEntity<Integer> addEventByOrganizer(@RequestBody @Valid CreateEventRequest eventRequest){
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(eventService.addEventByOrganizer(eventRequest).getId());
    }

    // TODO: Return images in response
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@Min(1) @PathVariable("id") Integer id,
                                                     @Valid EventRequest eventRequest) {
        return ResponseEntity.ok().body(EventMapper.eventToEventResponse(eventService.updateEvent(id, eventRequest)));
    }

    @SuppressWarnings("java:S107")
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllOrFilteredEvents(@Min(0) @RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @Min(0) @Max(50) @RequestParam(value = "size", defaultValue = "15") int size,
                                                                      @RequestParam(required = false) Integer parentId,
                                                                      @RequestParam(required = false) String title,
                                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                                      @RequestParam(required = false) EventStatus status,
                                                                      @RequestParam(required = false) EventFormat format) {
        return ResponseEntity.ok().body(EventMapper.eventsToEventResponseList(
                eventService.getAllOrFilteredEvents(page, size, parentId, title, startDate, endDate, status, format)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@Min(1) @PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(EventMapper.eventToEventResponse(eventService.getEventById(id)));
    }

    // TODO: Add annotation @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventById(@Min(1) @PathVariable("id") Integer id) {
        eventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/organizers")
    public ResponseEntity<List<UserRoleResponse>> getUsersHavingRoles(@Min(1) @PathVariable("id") Integer id) {
        List<EventRole> eventRoles = eventService.getUsersHavingRoles(id);
        return ResponseEntity.ok().body(EventRoleMapper.eventRolesToUserRoleResponses(eventRoles));
    }

    @PostMapping("/{id}/copy")
    public ResponseEntity<Integer> copyEvent(
            @Min(1) @PathVariable("id") Integer id,
            @RequestParam(value = "deep", defaultValue = "false") boolean deep) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.copyEvent(id, deep).getId());
    }
}
