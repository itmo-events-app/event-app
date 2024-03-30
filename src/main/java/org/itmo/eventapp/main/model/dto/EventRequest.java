package org.itmo.eventapp.main.model.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventFormat;
import org.itmo.eventapp.main.model.entity.EventStatus;
import org.itmo.eventapp.main.model.entity.Place;

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
