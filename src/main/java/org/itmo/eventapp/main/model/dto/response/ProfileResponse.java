package org.itmo.eventapp.main.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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
}
