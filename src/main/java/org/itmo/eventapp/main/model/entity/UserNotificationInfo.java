package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserNotificationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar[]", name = "devices")
    private String[] devices;

    private boolean enablePushNotifications;

    private boolean enableEmailNotifications;
}
