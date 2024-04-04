package org.itmo.eventapp.main.model.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
        Integer id,
        String title,
        String description,
        boolean seen,
        LocalDateTime sent_time
) {
}
