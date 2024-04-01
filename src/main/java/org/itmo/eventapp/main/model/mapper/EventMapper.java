package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;

import java.util.List;

public final class EventMapper {
    private EventMapper() {
    }

    public static EventResponse eventToEventResponse(Event event) {
        Integer parent = (event.getParent() != null) ? event.getParent().getId() : null;
        return new EventResponse(
                event.getId(),
                event.getPlace().getId(),
                event.getStartDate(),
                event.getEndDate(),
                event.getTitle(),
                event.getShortDescription(),
                event.getFullDescription(),
                event.getFormat(),
                event.getStatus(),
                event.getRegistrationStart(),
                event.getRegistrationEnd(),
                parent,
                event.getParticipantLimit(),
                event.getParticipantAgeLowest(),
                event.getParticipantAgeHighest(),
                event.getPreparingStart(),
                event.getPreparingEnd()
        );
    }

    public static Event eventRequestToEvent(Integer id, EventRequest eventRequest, Place place, Event parent) {
        return Event.builder()
                .id(id)
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
    }

    public static List<EventResponse> eventsToEventResponseList(List<Event> events) {
        return events.stream()
                .map(EventMapper::eventToEventResponse)
                .toList();
    }
}
