package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_notification_info")
@Getter
@Setter
public class UserNotificationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar[]", name = "devices")
    private String[] devices;

    private String surname;

    @Column(name = "enable_push_notifications")
    private String enablePushNotifications;

    @Column(name = "enable_email_notifications")
    private String enableEmailNotifications;

    public UserNotificationInfo() {
    }

    public UserNotificationInfo(String[] devices, String surname, String enablePushNotifications, String enableEmailNotifications) {
        this.devices = devices;
        this.surname = surname;
        this.enablePushNotifications = enablePushNotifications;
        this.enableEmailNotifications = enableEmailNotifications;
    }
}
