package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_t")
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

    // add EntityGraph
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_info_id")
    private UserNotificationInfo userNotificationInfo;

    // add EntityGraph
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private UserLoginInfo userLoginInfo;

    private String name;

    private String surname;
}
