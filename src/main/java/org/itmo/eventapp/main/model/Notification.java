package org.itmo.eventapp.main.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String description;

    private boolean sent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "read_time")
    private LocalDateTime readTime;
}
