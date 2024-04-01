package org.itmo.eventapp.main.model.dto.response;

import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;

import java.time.LocalDateTime;

public record EventResponse(
        Integer id,
        Integer placeId,
        LocalDateTime start,
        LocalDateTime end,
        String title,
        String shortDescription,
        String fullDescription,
        EventFormat format,
        EventStatus status,
        LocalDateTime registrationStart,
        LocalDateTime registrationEnd,
        Integer parent,
        int participantLimit,
        int participantAgeLowest,
        int participantAgeHighest,
        LocalDateTime preparingStart,
        LocalDateTime preparingEnd
) {
}

