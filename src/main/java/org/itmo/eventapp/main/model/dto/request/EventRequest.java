package org.itmo.eventapp.main.model.dto.request;

import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;

import java.time.LocalDateTime;

// TODO: Add correct annotations
public record EventRequest(Integer placeId,
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
                            int participantsLimit,
                            int participantsAgeLowest,
                            int participantsAgeHighest,
                            LocalDateTime preparingStart,
                            LocalDateTime preparingEnd) {
}
