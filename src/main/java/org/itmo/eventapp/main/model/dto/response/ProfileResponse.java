package org.itmo.eventapp.main.model.dto.response;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record ProfileResponse(
    Integer userId,
    String name,
    String surname,
    List<UserInfoResponse> userInfo,
    LocalDateTime lastLoginDate,
    boolean enablePushNotifications,
    boolean enableEmailNotifications,
    String[] devices

) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileResponse that = (ProfileResponse) o;
        return enablePushNotifications == that.enablePushNotifications &&
            enableEmailNotifications == that.enableEmailNotifications &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(userInfo, that.userInfo) &&
            Objects.equals(lastLoginDate, that.lastLoginDate) &&
            Arrays.equals(devices, that.devices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, surname, userInfo, lastLoginDate, enablePushNotifications, enableEmailNotifications, Arrays.hashCode(devices));
    }

    @Override
    public String toString() {
        return "ProfileResponse{" +
            "userId=" + userId +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", userInfo=" + userInfo +
            ", lastLoginDate=" + lastLoginDate +
            ", enablePushNotifications=" + enablePushNotifications +
            ", enableEmailNotifications=" + enableEmailNotifications +
            ", devices=" + Arrays.toString(devices) +
            '}';
    }
}