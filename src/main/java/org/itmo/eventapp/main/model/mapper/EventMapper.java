package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.request.EditEventRequest;
import org.itmo.eventapp.main.model.dto.request.EventRequest;
import org.itmo.eventapp.main.model.dto.response.EventResponse;
import org.itmo.eventapp.main.model.dto.response.EventShortDataResponse;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventRole;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.entity.PlaceRow;

import java.util.ArrayList;
import java.util.List;

public final class EventMapper {
    private EventMapper() {
    }

    public static EventResponse eventToEventResponse(Event event) {
        Integer parent = (event.getParent() != null) ? event.getParent().getId() : null;
        List<Integer>  placesIds = new ArrayList<>();
        if(event.getPlaces() != null){
            for(PlaceRow row : event.getPlaces()){
                placesIds.add(row.getPlace().getId());
            }
        }
        else{
            placesIds = null;
        }
        return new EventResponse(
            event.getId(),
            placesIds,
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

    public static EventShortDataResponse eventToEventShortDataResponse(Event event) {
        boolean isActivity = event.getParent() != null;
        if (isActivity) {
            return new EventShortDataResponse(
                event.getParent().getId(),
                event.getId(),
                event.getParent().getTitle(),
                event.getTitle()
            );
        }
        return new EventShortDataResponse(
            event.getId(),
            null,
            event.getTitle(),
            null
        );
    }

    public static Event eventRequestToEvent(Integer id, EventRequest eventRequest, List<PlaceRow> places, Event parent) {
        return Event.builder()
            .id(id)
            .places(places)
            .startDate(eventRequest.startDate())
            .endDate(eventRequest.endDate())
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

    public static Event editEventRequestToEvent(Integer id, EditEventRequest eventRequest, List<PlaceRow> places, Event parent) {
        return Event.builder()
                .id(id)
                .places(places)
                .startDate(eventRequest.startDate())
                .endDate(eventRequest.endDate())
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

    public static Event eventToEvent(Event source, Event parentEvent) {
        if (source == null) {
            return null;
        }
        return Event.builder()
            .places(source.getPlaces())
            .startDate(source.getStartDate())
            .endDate(source.getEndDate())
            .title(source.getTitle())
            .shortDescription(source.getShortDescription())
            .fullDescription(source.getFullDescription())
            .format(source.getFormat())
            .status(source.getStatus())
            .registrationStart(source.getRegistrationStart())
            .registrationEnd(source.getRegistrationEnd())
            .parent(parentEvent)
            .participantLimit(source.getParticipantLimit())
            .participantAgeLowest(source.getParticipantAgeLowest())
            .participantAgeHighest(source.getParticipantAgeHighest())
            .preparingStart(source.getPreparingStart())
            .preparingEnd(source.getPreparingEnd())
            .build();
    }

    public static List<Event> eventRolesToEvents(List<EventRole> eventRoles) {
        return eventRoles.stream()
            .map(EventMapper::eventRoleToEvent)
            .toList();
    }

    public static Event eventRoleToEvent(EventRole eventRole) {
        return Event.builder()
            .id(eventRole.getEvent().getId())
            .places(eventRole.getEvent().getPlaces())
            .startDate(eventRole.getEvent().getStartDate())
            .endDate(eventRole.getEvent().getEndDate())
            .title(eventRole.getEvent().getTitle())
            .shortDescription(eventRole.getEvent().getShortDescription())
            .fullDescription(eventRole.getEvent().getFullDescription())
            .format(eventRole.getEvent().getFormat())
            .status(eventRole.getEvent().getStatus())
            .registrationStart(eventRole.getEvent().getRegistrationStart())
            .registrationEnd(eventRole.getEvent().getRegistrationEnd())
            .parent(eventRole.getEvent().getParent())
            .participantLimit(eventRole.getEvent().getParticipantLimit())
            .participantAgeLowest(eventRole.getEvent().getParticipantAgeLowest())
            .participantAgeHighest(eventRole.getEvent().getParticipantAgeHighest())
            .preparingStart(eventRole.getEvent().getPreparingStart())
            .preparingEnd(eventRole.getEvent().getPreparingEnd())
            .build();
    }
}
