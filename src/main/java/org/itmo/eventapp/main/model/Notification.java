package org.itmo.eventapp.main.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "sent")
    private boolean sent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "read_time")
    private Date readTime;
}
