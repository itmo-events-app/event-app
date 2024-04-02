package org.itmo.eventapp.main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.exceptionhandling.ExceptionConst;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;
import org.itmo.eventapp.main.model.mapper.EventMapper;
import org.itmo.eventapp.main.repository.EventRepository;
import org.itmo.eventapp.main.repository.PlaceRepository;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Event addEvent(EventRequest eventRequest) {
        // TODO: Add privilege validation
        Place place = placeRepository.findById(eventRequest.placeId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.PLACE_NOT_FOUND_MESSAGE));

        Event parent = null;
        if (eventRequest.parent() != null) {
            parent = eventRepository.findById(eventRequest.parent()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_PARENT_NOT_FOUND_MESSAGE));
        }
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

    public Event findById(int id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE);
        }

        return event.get();
    }

    public EventResponse updateEvent(Integer id, EventRequest eventRequest) {
        Event parentEvent = null;
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE);
        }
        Place place = placeRepository.findById(eventRequest.placeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.PLACE_NOT_FOUND_MESSAGE));
        if (eventRequest.parent() != null) {
            parentEvent = eventRepository.findById(eventRequest.parent())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_PARENT_NOT_FOUND_MESSAGE));
        }
        Event updatedEvent = EventMapper.eventRequestToEvent(id, eventRequest, place, parentEvent);
        eventRepository.save(updatedEvent);
        return EventMapper.eventToEventResponse(updatedEvent);
    }

    public List<EventResponse> getAllOrFilteredEvents(int page, int size, String title,
                                                      LocalDateTime startDate, LocalDateTime endDate,
                                                      EventStatus status, EventFormat format) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (title != null) {
            predicates.add(cb.equal(root.get("title"), title));
        }
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), endDate));
        }
        if (startDate != null && endDate != null) {
            predicates.add(cb.between(root.get("startDate"), startDate, endDate));
        }
        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }
        if (format != null) {
            predicates.add(cb.equal(root.get("format"), format));
        }

        query.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Event> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        return EventMapper.eventsToEventResponseList(typedQuery.getResultList());
    }

    public EventResponse getEventById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionConst.EVENT_NOT_FOUND_MESSAGE));
        return EventMapper.eventToEventResponse(event);
    }

    public void deleteEventById(Integer id) {
        eventRepository.deleteById(id);
    }
}
