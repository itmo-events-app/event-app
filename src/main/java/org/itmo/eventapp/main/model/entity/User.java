package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "login_info_id")
    private UserLoginInfo userLoginInfo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<EventRole> userEvents;

    private String name;

    private String surname;
}
