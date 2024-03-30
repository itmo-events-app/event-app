package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "task")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigner_id")
    private User assigner;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status")
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;

    private LocalDateTime deadline;

    @Column(name = "notification_deadline")
    private LocalDateTime notificationDeadline;

    public Task() {
    }

    public Task(
            Event event,
            User assignee,
            User assigner,
            String title,
            String description,
            TaskStatus taskStatus,
            Place place,
            LocalDateTime deadline,
            LocalDateTime notificationDeadline) {
        this.event = event;
        this.assignee = assignee;
        this.assigner = assigner;
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.place = place;
        this.deadline = deadline;
        this.notificationDeadline = notificationDeadline;
    }
}
