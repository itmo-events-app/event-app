package org.itmo.eventapp.main.model.dto.request;

public record ParticipantPresenceRequest (
        Integer participantId,
        boolean isVisited
){
}
