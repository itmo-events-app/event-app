package org.itmo.eventapp.main.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_notification_info")
public class UserNotificationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar[]", name = "devices")
    private String[] devices;

    @Column(name = "surname")
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

    public Integer getId() {
        return id;
    }

    public String[] getDevices() {
        return devices;
    }

    public void setDevices(String[] devices) {
        this.devices = devices;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEnablePushNotifications() {
        return enablePushNotifications;
    }

    public void setEnablePushNotifications(String enablePushNotifications) {
        this.enablePushNotifications = enablePushNotifications;
    }

    public String getEnableEmailNotifications() {
        return enableEmailNotifications;
    }

    public void setEnableEmailNotifications(String enableEmailNotifications) {
        this.enableEmailNotifications = enableEmailNotifications;
    }
}
