package org.itmo.eventapp.main.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notifications_info_id")
    private UserNotificationInfo userNotificationInfo;

    @OneToMany(mappedBy = "employer", fetch = FetchType.LAZY)
    private List<EventRole> userEvents;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    public User() {
    }

    public User(Role role, UserNotificationInfo userNotificationInfo, String name, String surname) {
        this.role = role;
        this.userNotificationInfo = userNotificationInfo;
        this.name = name;
        this.surname = surname;
    }

    public Integer getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserNotificationInfo getUserNotificationInfo() {
        return userNotificationInfo;
    }

    public void setUserNotificationInfo(UserNotificationInfo userNotificationInfo) {
        this.userNotificationInfo = userNotificationInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<EventRole> getUserEvents() {
        return userEvents;
    }

    public void setUserEvents(List<EventRole> userEvents) {
        this.userEvents = userEvents;
    }
}
