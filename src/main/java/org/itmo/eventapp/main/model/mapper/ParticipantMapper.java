package org.itmo.eventapp.main.model.mapper;

import org.itmo.eventapp.main.model.dto.response.ParticipantResponse;
import org.itmo.eventapp.main.model.entity.Participant;

import java.util.List;

public final class ParticipantMapper {
    private ParticipantMapper(){

    }
    public static ParticipantResponse participantToResponse(Participant participant) {
        return new ParticipantResponse(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getAdditionalInfo(),
                participant.isVisited(),
                participant.getEvent().getId()
        );
    }
    public static List<ParticipantResponse> participantsToResponseList(List<Participant> participants) {
        return participants.stream()
                .map(ParticipantMapper::participantToResponse)
                .toList();
    }
}
