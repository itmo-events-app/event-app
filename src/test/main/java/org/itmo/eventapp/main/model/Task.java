package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Date;

@Converter(autoApply = true)
class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {
    @Override
    public String convertToDatabaseColumn(TaskStatus color) {
        if (color == null) {
            return null;
        }
        return color.name().toLowerCase();
    }

    @Override
    public TaskStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return TaskStatus.valueOf(value.toUpperCase());
    }
}


@Entity
@Table(name = "task")
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

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "task_status", name = "status")
    @Convert(converter = TaskStatusConverter.class)
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deadline")
    private Date deadline;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notification_deadline")
    private Date notificationDeadline;

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
            Date deadline,
            Date notificationDeadline) {
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

    public Integer getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getNotificationDeadline() {
        return notificationDeadline;
    }

    public void setNotificationDeadline(Date notificationDeadline) {
        this.notificationDeadline = notificationDeadline;
    }
}
