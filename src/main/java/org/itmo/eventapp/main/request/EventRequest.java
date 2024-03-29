package org.itmo.eventapp.main.request;

import jakarta.persistence.*;
import lombok.Data;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.EventFormat;
import org.itmo.eventapp.main.model.entity.EventStatus;
import org.itmo.eventapp.main.model.entity.Place;

import java.time.LocalDateTime;

@Data
public class EventRequest {
    private Integer placeId;

    private LocalDateTime start;

    private LocalDateTime end;

    private String title;

    private String shortDescription;

    private String fullDescription;

    private EventFormat format;

    private EventStatus status;

    private LocalDateTime registrationStart;

    private LocalDateTime registrationEnd;

    private Integer parent;

    private int participantsLimit;

    private int participantsAgeLowest;

    private int participantsAgeHighest;

    private LocalDateTime preparingStart;

    private LocalDateTime preparingEnd;

}
