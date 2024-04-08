package org.itmo.eventapp.main.model.dto.response;

public record ParticipantResponse(
    Integer id,
    String name,
    String email,
    String additionalInfo,
    boolean visited,
    Integer eventId
)
{
}
