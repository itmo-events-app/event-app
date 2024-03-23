package org.itmo.eventapp.main.model;


import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Date;

@Converter(autoApply = true)
class EventFormatConverter implements AttributeConverter<EventFormat, String> {
    @Override
    public String convertToDatabaseColumn(EventFormat color) {
        if (color == null) {
            return null;
        }
        return color.name().toLowerCase();
    }

    @Override
    public EventFormat convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return EventFormat.valueOf(value.toUpperCase());
    }
}

@Converter(autoApply = true)
class EventStatusConverter implements AttributeConverter<EventStatus, String> {
    @Override
    public String convertToDatabaseColumn(EventStatus color) {
        if (color == null) {
            return null;
        }
        return color.name().toLowerCase();
    }

    @Override
    public EventStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return EventStatus.valueOf(value.toUpperCase());
    }
}

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start")
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end")
    private Date end;

    @Column(name = "title")
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "full_description")
    private String fullDescription;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "event_format", name = "format")
    @Convert(converter = EventFormatConverter.class)
    private EventFormat format;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "event_status", name = "status")
    @Convert(converter = EventStatusConverter.class)
    private EventStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_start")
    private Date registrationStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_end")
    private Date registrationEnd;

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
    private Date preparingStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "preparing_end")
    private Date preparingEnd;

    public Event() {
    }

    public Event(
            Place place,
            Date start,
            Date end,
            String title,
            String shortDescription,
            String fullDescription,
            EventFormat format,
            EventStatus status,
            Date registrationStart,
            Date registrationEnd,
            Event parent,
            int participantsLimit,
            int participantsAgeLowest,
            int participantsAgeHighest,
            Date preparingStart,
            Date preparingEnd) {
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

    public Integer getId() {
        return id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public EventFormat getFormat() {
        return format;
    }

    public void setFormat(EventFormat format) {
        this.format = format;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Date getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(Date registrationStart) {
        this.registrationStart = registrationStart;
    }

    public Date getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(Date registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public Event getParent() {
        return parent;
    }

    public void setParent(Event parent) {
        this.parent = parent;
    }

    public int getParticipantsLimit() {
        return participantsLimit;
    }

    public void setParticipantsLimit(int participantsLimit) {
        this.participantsLimit = participantsLimit;
    }

    public int getParticipantsAgeLowest() {
        return participantsAgeLowest;
    }

    public void setParticipantsAgeLowest(int participantsAgeLowest) {
        this.participantsAgeLowest = participantsAgeLowest;
    }

    public int getParticipantsAgeHighest() {
        return participantsAgeHighest;
    }

    public void setParticipantsAgeHighest(int participantsAgeHighest) {
        this.participantsAgeHighest = participantsAgeHighest;
    }

    public Date getPreparingStart() {
        return preparingStart;
    }

    public void setPreparingStart(Date preparingStart) {
        this.preparingStart = preparingStart;
    }

    public Date getPreparingEnd() {
        return preparingEnd;
    }

    public void setPreparingEnd(Date preparingEnd) {
        this.preparingEnd = preparingEnd;
    }
}
