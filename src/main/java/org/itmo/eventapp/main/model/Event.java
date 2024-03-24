package org.itmo.eventapp.main.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "event")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime end;

    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "full_description")
    private String fullDescription;

    @Enumerated(EnumType.STRING)
    private EventFormat format;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_start")
    private LocalDateTime registrationStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_end")
    private LocalDateTime registrationEnd;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Event parent;

    @Column(name = "participants_limit")
    private int participantsLimit;

    @Column(name = "participants_age_lowest")
    private int participantsAgeLowest;

    @Column(name = "participants_age_highest")
    private int participantsAgeHighest;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "preparing_start")
    private LocalDateTime preparingStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "preparing_end")
    private LocalDateTime preparingEnd;

    public Event() {
    }

    public Event(
            Place place,
            LocalDateTime start,
            LocalDateTime end,
            String title,
            String shortDescription,
            String fullDescription,
            EventFormat format,
            EventStatus status,
            LocalDateTime registrationStart,
            LocalDateTime registrationEnd,
            Event parent,
            int participantsLimit,
            int participantsAgeLowest,
            int participantsAgeHighest,
            LocalDateTime preparingStart,
            LocalDateTime preparingEnd) {
        this.place = place;
        this.start = start;
        this.end = end;
        this.title = title;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.format = format;
        this.status = status;
        this.registrationStart = registrationStart;
        this.registrationEnd = registrationEnd;
        this.parent = parent;
        this.participantsLimit = participantsLimit;
        this.participantsAgeLowest = participantsAgeLowest;
        this.participantsAgeHighest = participantsAgeHighest;
        this.preparingStart = preparingStart;
        this.preparingEnd = preparingEnd;
    }
}
