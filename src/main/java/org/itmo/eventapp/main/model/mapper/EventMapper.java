package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.User;

public final class EventMapper {
    private EventMapper() {
    }

    public static EventResponse eventToEventResponse(Event event) {
        Event parent = event.getParent();
        Integer parentId = parent == null ? null : parent.getId();
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
                parentId,
                event.getParticipantLimit(),
                event.getParticipantAgeLowest(),
                event.getParticipantAgeHighest(),
                event.getPreparingStart(),
                event.getPreparingEnd()
        );
    }

    public static Event eventRequestToEvent(EventRequest eventRequest, Place place, Event parent) {
        return Event.builder()
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
}
