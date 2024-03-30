package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_info_id")
    private UserNotificationInfo userNotificationInfo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<EventRole> userEvents;

    private String name;

    private String surname;

    public User() {
    }

    public User(Role role, UserNotificationInfo userNotificationInfo, String name, String surname) {
        this.role = role;
        this.userNotificationInfo = userNotificationInfo;
        this.name = name;
        this.surname = surname;
    }
}
