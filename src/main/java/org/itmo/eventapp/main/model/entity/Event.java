package org.itmo.eventapp.main.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.itmo.eventapp.main.model.entity.enums.EventFormat;
import org.itmo.eventapp.main.model.entity.enums.EventStatus;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;

    private LocalDateTime start;

    @Column(name = "\"end\"")
    private LocalDateTime end;

    private String title;

    private String shortDescription;

    private String fullDescription;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EventFormat format;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EventStatus status;

    private LocalDateTime registrationStart;

    private LocalDateTime registrationEnd;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Event parent;

    private int participantLimit;

    private int participantAgeLowest;

    private int participantAgeHighest;

    private LocalDateTime preparingStart;

    private LocalDateTime preparingEnd;
}
